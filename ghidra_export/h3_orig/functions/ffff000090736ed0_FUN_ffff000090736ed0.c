// ffff000090736ed0  size=444  FUN_ffff000090736ed0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090736ed0(long param_1)

{
  int iVar1;
  undefined1 *puVar2;
  
  puVar2 = (undefined1 *)0x0;
  if (param_1 != 0) {
    if ((_DAT_ffff00009086dc00 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc00,param_1), iVar1 != 0)) {
      if ((_DAT_ffff00009086dc18 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc18,param_1), iVar1 != 0)) {
        if ((_DAT_ffff00009086dc30 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc30,param_1), iVar1 != 0)) {
          if ((_DAT_ffff00009086dc48 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc48,param_1), iVar1 != 0)) {
            if ((_DAT_ffff00009086dc60 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc60,param_1), iVar1 != 0)) {
              if ((_DAT_ffff00009086dc78 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc78,param_1), iVar1 != 0)) {
                if ((_DAT_ffff00009086dc90 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc90,param_1), iVar1 != 0)) {
                  if ((_DAT_ffff00009086dca8 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dca8,param_1), iVar1 != 0)) {
                    if ((_DAT_ffff00009086dcc0 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dcc0,param_1), iVar1 != 0)) {
                      if ((_DAT_ffff00009086dcd8 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dcd8,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined1 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff00009086dcd8;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff00009086dcc0;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff00009086dca8;
                  }
                }
                else {
                  puVar2 = &DAT_ffff00009086dc90;
                }
              }
              else {
                puVar2 = &DAT_ffff00009086dc78;
              }
            }
            else {
              puVar2 = &DAT_ffff00009086dc60;
            }
          }
          else {
            puVar2 = &DAT_ffff00009086dc48;
          }
        }
        else {
          puVar2 = &DAT_ffff00009086dc30;
        }
      }
      else {
        puVar2 = &DAT_ffff00009086dc18;
      }
    }
    else {
      puVar2 = &DAT_ffff00009086dc00;
    }
  }
  return puVar2;
}


