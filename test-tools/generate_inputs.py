#!/usr/bin/env python3
# encoding: utf-8
'''
generate_inputs -- Generates input files for DPF 

generate_inputs is a description

It defines classes_and_methods

@author:     Ersan Demircioglu

@copyright:  2022 Ersan Demircioglu. All rights reserved.

@license:    Apache License 2.0

'''

import sys
import os

from argparse import ArgumentParser
from argparse import RawDescriptionHelpFormatter
from duplicity.tempdir import default
import json
import datetime
import time

__all__ = []
__version__ = 0.1
__date__ = '2022-12-08'
__updated__ = '2022-12-08'

global_simulation_time = datetime.datetime.now()
satellites = {}

class CLIError(Exception):
    '''Generic exception to raise and log different fatal errors.'''
    def __init__(self, msg):
        super(CLIError).__init__(type(self))
        self.msg = "E: %s" % msg
    def __str__(self):
        return self.msg
    def __unicode__(self):
        return self.msg
    
class Satellite:
    def __init__(self, satellite_conf, simulation_start_time):
        self.name = satellite_conf["name"]
        self.orbit = satellite_conf["initial_orbit"]
        self.orbit_duration = satellite_conf["orbit_duration"]
        self.orbit_start_time = simulation_start_time 
            
    def propagate(self, current_simulation_time):
        if self.orbit_start_time + datetime.timedelta(minutes=self.orbit_duration) < current_simulation_time:
            self.orbit_start_time = current_simulation_time
            self.orbit += 1
            print(f"New Orbit for {self.name}: {self.orbit}")

    def getOrbitNumber(self):
        return self.orbit
    
class SatDataTemplate:
    def __init__(self, template_conf, simulation_start_time):
        self.name = template_conf["name"]
        self.filename_template = template_conf["filename_template"]
        self.variables = template_conf["variables"]
        self.frequency = template_conf["frequency"]
        self.first_pass = template_conf["first_pass"]
        self.pass_duration = template_conf["pass_duration"]
        self.segment_duration = template_conf["segment_duration"]
        self.generate_end_file = template_conf["generate_end_file"]
        self.pass_start = simulation_start_time + datetime.timedelta(minutes=self.first_pass)
        self.pass_end = self.pass_start + datetime.timedelta(minutes=self.pass_duration)
        self.last_generation_time = self.pass_start
        
    def generate(self, current_simulation_time):
        if self.__is_in_pass(current_simulation_time) \
            and self.last_generation_time + datetime.timedelta(minutes=self.segment_duration) <= current_simulation_time:
            self.__generate_file(current_simulation_time, False)

    def __generate_file(self, current_simulation_time, is_end_file):
        dict = {}
        for key, value in self.variables.items():
            dict[key] = eval(value)
        s = self.filename_template.format(**dict)
        self.last_generation_time = current_simulation_time
        if is_end_file:
            createFile(s+".END")
        else:
            createFile(s)
            
    def __is_in_pass(self, current_simulation_time):
        if self.pass_start <= current_simulation_time and current_simulation_time <= self.pass_end:
            return True
        elif self.pass_end < current_simulation_time: #pass is ended, new pass must be calculated
            self.pass_start = current_simulation_time + datetime.timedelta(minutes=self.frequency)
            self.pass_end = self.pass_start + datetime.timedelta(minutes=self.pass_duration)
            self.last_generation_time = self.pass_start
            if self.generate_end_file:
                self.__generate_file(current_simulation_time, True)
            print(f"Next Pass for {self.name}: {self.pass_start}-{self.pass_end}")
        return False

class AuxDataTemplate:
    def __init__(self, template_conf, simulation_start_time):
        self.filename_template = template_conf["filename_template"]
        self.variables = template_conf["variables"]
        self.frequency = template_conf["frequency"]
        self.first_generation = template_conf["first_generation"]
        self.next_generation_time = simulation_start_time + datetime.timedelta(minutes=self.first_generation)
        
    def generate(self, current_simulation_time):
        if self.next_generation_time <= current_simulation_time:
            dict = {}
            for key, value in self.variables.items():
                dict[key] = eval(value)
            s = self.filename_template.format(**dict)
            self.next_generation_time = current_simulation_time + datetime.timedelta(minutes=self.frequency)
            createFile(s)
        
def createFile(filename):
    print(filename)
    try:
        with open(f"{dest_folder}/{filename}", 'w') as f:
            f.write(filename)
    except FileNotFoundError:
        print(f"Failed to create {filename}")
    
def orbit(sat):
    satellite = satellites[sat]
    return satellite.getOrbitNumber()

def simulation_time():
    return global_simulation_time

def parse_conf(conf_path):
    conf_file = open(conf_path)
    conf = json.load(conf_file)
    conf_file.close()
    return conf
    
def start(args):
    global global_simulation_time
    global satellites
    global dest_folder
    
    print(args.verbose)
    print(args.config)
    print(args.dest)
    dest_folder = args.dest
    global_simulation_time = datetime.datetime.now()
    conf = parse_conf(args.config)
    
    for satellite_conf in conf["satellites"]:
        satellite = Satellite(satellite_conf, global_simulation_time)
        satellites[satellite.name] = satellite
    
    sat_data_template_list = []
    for template_conf in conf["sat_data_templates"]:
        sat_data_template_list.append(SatDataTemplate(template_conf, global_simulation_time))
    
    aux_data_template_list = []
    for template_conf in conf["aux_data_templates"]:
        aux_data_template_list.append(AuxDataTemplate(template_conf, global_simulation_time))


    for time_tick in range(60*24):
        print(f"Simulation time : {global_simulation_time}")
        for satellite in satellites.values():
            satellite.propagate(global_simulation_time)
  
        for sat_data_template in sat_data_template_list:
            sat_data_template.generate(global_simulation_time)
        
        for aux_data_template in aux_data_template_list:
            aux_data_template.generate(global_simulation_time)
        
        global_simulation_time = global_simulation_time + datetime.timedelta(minutes=1)
        time.sleep(1)
    
def main(argv=None): # IGNORE:C0111
    '''Command line options.'''

    if argv is None:
        argv = sys.argv
    else:
        sys.argv.extend(argv)

    program_name = os.path.basename(sys.argv[0])
    program_version = "v%s" % __version__
    program_build_date = str(__updated__)
    program_version_message = '%%(prog)s %s (%s)' % (program_version, program_build_date)
    program_shortdesc = __import__('__main__').__doc__.split("\n")[1]
    program_license = '''%s

  Created by Ersan Demircioglu on %s.
  Copyright 2022 organization_name. All rights reserved.

  Licensed under the Apache License 2.0
  http://www.apache.org/licenses/LICENSE-2.0

  Distributed on an "AS IS" basis without warranties
  or conditions of any kind, either express or implied.

USAGE
''' % (program_shortdesc, str(__date__))

    try:
        # Setup argument parser
        parser = ArgumentParser(description=program_license, formatter_class=RawDescriptionHelpFormatter)
        parser.add_argument("-v", "--verbose", dest="verbose", action='store_true', help="set verbosity level")
        parser.add_argument("-c", "--config", dest="config", help="Configuration JSON file. [default: %(default)s]")
        parser.add_argument("-d", "--dest", dest="dest", help="Destination folder (DPF's incoming folder). [default: %(default)s]" )
        parser.add_argument('-V', '--version', action='version', version=program_version_message)

        # Process arguments
        args = parser.parse_args()
        start(args)
        return 0
    except KeyboardInterrupt:
        ### handle keyboard interrupt ###
        return 0
    except Exception as e:
        indent = len(program_name) * " "
        sys.stderr.write(program_name + ": " + repr(e) + "\n")
        sys.stderr.write(indent + "  for help use --help")
        return 2

if __name__ == "__main__":
    sys.exit(main())