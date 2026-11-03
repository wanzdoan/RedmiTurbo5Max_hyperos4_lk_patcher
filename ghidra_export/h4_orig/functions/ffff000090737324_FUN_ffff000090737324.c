// ffff000090737324  size=504  FUN_ffff000090737324

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

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
  if (_DAT_ffff00009086dc98 == (undefined8 *)0x0) {
    lVar3 = 0;
    puVar2 = (undefined8 *)&DAT_ffff00009086dc98;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dc98,*param_3);
    if (iVar1 == 0) {
      return 0xfffffff8;
    }
    puVar2 = (undefined8 *)&DAT_ffff00009086dcb0;
    if (_DAT_ffff00009086dcb0 == (undefined8 *)0x0) {
      lVar3 = 1;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcb0,*param_3);
      if (iVar1 == 0) {
        return 0xfffffff8;
      }
      puVar2 = (undefined8 *)&DAT_ffff00009086dcc8;
      if (_DAT_ffff00009086dcc8 == (undefined8 *)0x0) {
        lVar3 = 2;
      }
      else {
        iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcc8,*param_3);
        if (iVar1 == 0) {
          return 0xfffffff8;
        }
        puVar2 = (undefined8 *)&DAT_ffff00009086dce0;
        if (_DAT_ffff00009086dce0 == (undefined8 *)0x0) {
          lVar3 = 3;
        }
        else {
          iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dce0,*param_3);
          if (iVar1 == 0) {
            return 0xfffffff8;
          }
          puVar2 = (undefined8 *)&DAT_ffff00009086dcf8;
          if (_DAT_ffff00009086dcf8 == (undefined8 *)0x0) {
            lVar3 = 4;
          }
          else {
            iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dcf8,*param_3);
            if (iVar1 == 0) {
              return 0xfffffff8;
            }
            puVar2 = (undefined8 *)&DAT_ffff00009086dd10;
            if (_DAT_ffff00009086dd10 == (undefined8 *)0x0) {
              lVar3 = 5;
            }
            else {
              iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd10,*param_3);
              if (iVar1 == 0) {
                return 0xfffffff8;
              }
              puVar2 = (undefined8 *)&DAT_ffff00009086dd28;
              if (_DAT_ffff00009086dd28 == (undefined8 *)0x0) {
                lVar3 = 6;
              }
              else {
                iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd28,*param_3);
                if (iVar1 == 0) {
                  return 0xfffffff8;
                }
                puVar2 = (undefined8 *)&DAT_ffff00009086dd40;
                if (_DAT_ffff00009086dd40 == (undefined8 *)0x0) {
                  lVar3 = 7;
                }
                else {
                  iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd40,*param_3);
                  if (iVar1 == 0) {
                    return 0xfffffff8;
                  }
                  puVar2 = (undefined8 *)&DAT_ffff00009086dd58;
                  if (_DAT_ffff00009086dd58 == (undefined8 *)0x0) {
                    lVar3 = 8;
                  }
                  else {
                    iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd58,*param_3);
                    if (iVar1 == 0) {
                      return 0xfffffff8;
                    }
                    puVar2 = (undefined8 *)&DAT_ffff00009086dd70;
                    if (_DAT_ffff00009086dd70 != (undefined8 *)0x0) {
                      iVar1 = FUN_ffff00009077f0ac(*_DAT_ffff00009086dd70,*param_3);
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
  *(long *)(&DAT_ffff00009086dca0 + lVar3 * 0x18) = param_1;
  *(undefined8 *)(&DAT_ffff00009086dca8 + lVar3 * 0x18) = param_2;
  return 0;
}


