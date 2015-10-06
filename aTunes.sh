#!/bin/bash

# aTunes
# Copyright (C) 2006-2007 Alex Aranda (fleax)
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

cd `dirname $0`

java -cp aTunes.jar:lib/commons-io-1.2.jar:lib/jcommon-1.0.0.jar:lib/jfreechart-1.0.1.jar:lib/jid3lib-0.5.4.jar:lib/entagged-audioformats-0.15.jar:lib/log4j-1.2.13.jar:lib/commons-logging-1.1.jar:lib/jdic.jar:lib/jdic_stub_linux.jar:lib/antBuildNumber.jar:lib/substance.jar:lib/oro.jar net.sourceforge.atunes.Main
