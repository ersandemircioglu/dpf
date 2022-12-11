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
            print("new Orbit {} {}".format(self.name, self.orbit))

    def getOrbitNumber(self):
        return self.orbit
    
class Template:
    def __init__(self, template_conf, simulation_start_time):
        self.filename_template = template_conf["filename_template"]
        self.variables = template_conf["variables"]
        self.frequency = template_conf["frequency"]
        self.first_pass = template_conf["first_pass"]
        self.pass_duration = template_conf["pass_duration"]
        self.segment_duration = template_conf["segment_duration"]
        self.pass_start = simulation_start_time + datetime.timedelta(minutes=self.first_pass)
        self.pass_end = self.pass_start + datetime.timedelta(minutes=self.pass_duration)
        self.last_generation_time = self.pass_start
        
        
    def generate(self, current_simulation_time):
        if self.__is_in_pass(current_simulation_time) \
            and self.last_generation_time + datetime.timedelta(minutes=self.segment_duration) <= current_simulation_time:
            dict = {}
            for key, value in self.variables.items():
                dict[key] = eval(value)
            s = self.filename_template.format(**dict)
            self.last_generation_time = current_simulation_time
            print(s)
        
    def __is_in_pass(self, current_simulation_time):
        if self.pass_start <= current_simulation_time and current_simulation_time <= self.pass_end:
            return True
        elif self.pass_end < current_simulation_time: #pass is ended, new pass must be calculated
            self.pass_start = current_simulation_time + datetime.timedelta(minutes=self.frequency)
            self.pass_end = self.pass_start + datetime.timedelta(minutes=self.pass_duration)
            self.last_generation_time = self.pass_start
            print("new Pass {} {}".format(self.pass_start, self.pass_end))
        return False

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
    
    print(args.verbose)
    print(args.config)
    print(args.dest)
    global_simulation_time = datetime.datetime.now()
    conf = parse_conf(args.config)
    
    for satellite_conf in conf["satellites"]:
        satellite = Satellite(satellite_conf, global_simulation_time)
        satellites[satellite.name] = satellite
    
    template_list = []
    for template_conf in conf["file_templates"]:
        template_list.append(Template(template_conf, global_simulation_time))
    
    for time_tick in range(60*24):
        print(global_simulation_time)
        for satellite in satellites.values():
            satellite.propagate(global_simulation_time)
  
        for template in template_list:
            template.generate(global_simulation_time)
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