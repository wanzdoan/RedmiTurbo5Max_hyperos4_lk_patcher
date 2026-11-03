// ffff000090737168  size=444  FUN_ffff000090737168

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090737168(long param_1)

{
  int iVar1;
  undefined1 *puVar2;
  
  puVar2 = (undefined1 *)0x0;
  if (param_1 != 0) {
    if ((_DAT_ffff00009086dc98 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dc98,param_1), iVar1 != 0)) {
      if ((_DAT_ffff00009086dcb0 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcb0,param_1), iVar1 != 0)) {
        if ((_DAT_ffff00009086dcc8 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcc8,param_1), iVar1 != 0)) {
          if ((_DAT_ffff00009086dce0 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dce0,param_1), iVar1 != 0)) {
            if ((_DAT_ffff00009086dcf8 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcf8,param_1), iVar1 != 0)) {
              if ((_DAT_ffff00009086dd10 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd10,param_1), iVar1 != 0)) {
                if ((_DAT_ffff00009086dd28 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd28,param_1), iVar1 != 0)) {
                  if ((_DAT_ffff00009086dd40 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd40,param_1), iVar1 != 0)) {
                    if ((_DAT_ffff00009086dd58 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd58,param_1), iVar1 != 0)) {
                      if ((_DAT_ffff00009086dd70 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd70,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined1 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff00009086dd70;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff00009086dd58;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff00009086dd40;
                  }
                }
                else {
                  puVar2 = &DAT_ffff00009086dd28;
                }
              }
              else {
                puVar2 = &DAT_ffff00009086dd10;
              }
            }
            else {
              puVar2 = &DAT_ffff00009086dcf8;
            }
          }
          else {
            puVar2 = &DAT_ffff00009086dce0;
          }
        }
        else {
          puVar2 = &DAT_ffff00009086dcc8;
        }
      }
      else {
        puVar2 = &DAT_ffff00009086dcb0;
      }
    }
    else {
      puVar2 = &DAT_ffff00009086dc98;
    }
  }
  return puVar2;
}


