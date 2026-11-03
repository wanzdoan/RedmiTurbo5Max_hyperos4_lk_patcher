// ffff000090736860  size=444  FUN_ffff000090736860

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090736860(long param_1)

{
  int iVar1;
  undefined1 *puVar2;
  
  puVar2 = (undefined1 *)0x0;
  if (param_1 != 0) {
    if ((_DAT_ffff00009086db58 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086db58,param_1), iVar1 != 0)) {
      if ((_DAT_ffff00009086db68 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086db68,param_1), iVar1 != 0)) {
        if ((_DAT_ffff00009086db78 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086db78,param_1), iVar1 != 0)) {
          if ((_DAT_ffff00009086db88 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086db88,param_1), iVar1 != 0)) {
            if ((_DAT_ffff00009086db98 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086db98,param_1), iVar1 != 0)) {
              if ((_DAT_ffff00009086dba8 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dba8,param_1), iVar1 != 0)) {
                if ((_DAT_ffff00009086dbb8 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dbb8,param_1), iVar1 != 0)) {
                  if ((_DAT_ffff00009086dbc8 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dbc8,param_1), iVar1 != 0)) {
                    if ((_DAT_ffff00009086dbd8 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dbd8,param_1), iVar1 != 0)) {
                      if ((_DAT_ffff00009086dbe8 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dbe8,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined1 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff00009086dbe8;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff00009086dbd8;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff00009086dbc8;
                  }
                }
                else {
                  puVar2 = &DAT_ffff00009086dbb8;
                }
              }
              else {
                puVar2 = &DAT_ffff00009086dba8;
              }
            }
            else {
              puVar2 = &DAT_ffff00009086db98;
            }
          }
          else {
            puVar2 = &DAT_ffff00009086db88;
          }
        }
        else {
          puVar2 = &DAT_ffff00009086db78;
        }
      }
      else {
        puVar2 = &DAT_ffff00009086db68;
      }
    }
    else {
      puVar2 = &DAT_ffff00009086db58;
    }
  }
  return puVar2;
}


