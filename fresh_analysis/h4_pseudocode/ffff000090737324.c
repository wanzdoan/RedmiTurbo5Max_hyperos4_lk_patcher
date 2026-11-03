
undefined4 FUN_ffff000090737324(long param_1,undefined8 param_2,undefined8 *param_3)

{
  int iVar1;
  undefined8 *puVar2;
  long lVar3;
  
  if (param_1 == 0) {
    return 0xfffffff8;
  }
  if (param_3 == (undefined8 *)0x0) {
    return 0xfffffff8;
  }
  if (DAT_ffff00009086dc98 == (undefined8 *)0x0) {
    lVar3 = 0;
    puVar2 = &DAT_ffff00009086dc98;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc98,*param_3);
    if (iVar1 == 0) {
      return 0xfffffff8;
    }
    puVar2 = &DAT_ffff00009086dcb0;
    if (DAT_ffff00009086dcb0 == (undefined8 *)0x0) {
      lVar3 = 1;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dcb0,*param_3);
      if (iVar1 == 0) {
        return 0xfffffff8;
      }
      puVar2 = &DAT_ffff00009086dcc8;
      if (DAT_ffff00009086dcc8 == (undefined8 *)0x0) {
        lVar3 = 2;
      }
      else {
        iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dcc8,*param_3);
        if (iVar1 == 0) {
          return 0xfffffff8;
        }
        puVar2 = &DAT_ffff00009086dce0;
        if (DAT_ffff00009086dce0 == (undefined8 *)0x0) {
          lVar3 = 3;
        }
        else {
          iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dce0,*param_3);
          if (iVar1 == 0) {
            return 0xfffffff8;
          }
          puVar2 = &DAT_ffff00009086dcf8;
          if (DAT_ffff00009086dcf8 == (undefined8 *)0x0) {
            lVar3 = 4;
          }
          else {
            iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dcf8,*param_3);
            if (iVar1 == 0) {
              return 0xfffffff8;
            }
            puVar2 = &DAT_ffff00009086dd10;
            if (DAT_ffff00009086dd10 == (undefined8 *)0x0) {
              lVar3 = 5;
            }
            else {
              iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dd10,*param_3);
              if (iVar1 == 0) {
                return 0xfffffff8;
              }
              puVar2 = &DAT_ffff00009086dd28;
              if (DAT_ffff00009086dd28 == (undefined8 *)0x0) {
                lVar3 = 6;
              }
              else {
                iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dd28,*param_3);
                if (iVar1 == 0) {
                  return 0xfffffff8;
                }
                puVar2 = &DAT_ffff00009086dd40;
                if (DAT_ffff00009086dd40 == (undefined8 *)0x0) {
                  lVar3 = 7;
                }
                else {
                  iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dd40,*param_3);
                  if (iVar1 == 0) {
                    return 0xfffffff8;
                  }
                  puVar2 = &DAT_ffff00009086dd58;
                  if (DAT_ffff00009086dd58 == (undefined8 *)0x0) {
                    lVar3 = 8;
                  }
                  else {
                    iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dd58,*param_3);
                    if (iVar1 == 0) {
                      return 0xfffffff8;
                    }
                    puVar2 = &DAT_ffff00009086dd70;
                    if (DAT_ffff00009086dd70 != (undefined8 *)0x0) {
                      iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dd70,*param_3);
                      if (iVar1 != 0) {
                        return 0;
                      }
                      return 0xfffffff8;
                    }
                    lVar3 = 9;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  *puVar2 = param_3;
  (&DAT_ffff00009086dca0)[lVar3 * 3] = param_1;
  (&DAT_ffff00009086dca8)[lVar3 * 3] = param_2;
  return 0;
}


