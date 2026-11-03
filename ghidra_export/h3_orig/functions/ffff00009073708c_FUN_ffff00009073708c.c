// ffff00009073708c  size=504  FUN_ffff00009073708c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009073708c(long param_1,undefined8 param_2,undefined8 *param_3)

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
  if (_DAT_ffff00009086dc00 == (undefined8 *)0x0) {
    lVar3 = 0;
    puVar2 = (undefined8 *)&DAT_ffff00009086dc00;
  }
  else {
    iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc00,*param_3);
    if (iVar1 == 0) {
      return 0xfffffff8;
    }
    puVar2 = (undefined8 *)&DAT_ffff00009086dc18;
    if (_DAT_ffff00009086dc18 == (undefined8 *)0x0) {
      lVar3 = 1;
    }
    else {
      iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc18,*param_3);
      if (iVar1 == 0) {
        return 0xfffffff8;
      }
      puVar2 = (undefined8 *)&DAT_ffff00009086dc30;
      if (_DAT_ffff00009086dc30 == (undefined8 *)0x0) {
        lVar3 = 2;
      }
      else {
        iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc30,*param_3);
        if (iVar1 == 0) {
          return 0xfffffff8;
        }
        puVar2 = (undefined8 *)&DAT_ffff00009086dc48;
        if (_DAT_ffff00009086dc48 == (undefined8 *)0x0) {
          lVar3 = 3;
        }
        else {
          iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc48,*param_3);
          if (iVar1 == 0) {
            return 0xfffffff8;
          }
          puVar2 = (undefined8 *)&DAT_ffff00009086dc60;
          if (_DAT_ffff00009086dc60 == (undefined8 *)0x0) {
            lVar3 = 4;
          }
          else {
            iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc60,*param_3);
            if (iVar1 == 0) {
              return 0xfffffff8;
            }
            puVar2 = (undefined8 *)&DAT_ffff00009086dc78;
            if (_DAT_ffff00009086dc78 == (undefined8 *)0x0) {
              lVar3 = 5;
            }
            else {
              iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc78,*param_3);
              if (iVar1 == 0) {
                return 0xfffffff8;
              }
              puVar2 = (undefined8 *)&DAT_ffff00009086dc90;
              if (_DAT_ffff00009086dc90 == (undefined8 *)0x0) {
                lVar3 = 6;
              }
              else {
                iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dc90,*param_3);
                if (iVar1 == 0) {
                  return 0xfffffff8;
                }
                puVar2 = (undefined8 *)&DAT_ffff00009086dca8;
                if (_DAT_ffff00009086dca8 == (undefined8 *)0x0) {
                  lVar3 = 7;
                }
                else {
                  iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dca8,*param_3);
                  if (iVar1 == 0) {
                    return 0xfffffff8;
                  }
                  puVar2 = (undefined8 *)&DAT_ffff00009086dcc0;
                  if (_DAT_ffff00009086dcc0 == (undefined8 *)0x0) {
                    lVar3 = 8;
                  }
                  else {
                    iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dcc0,*param_3);
                    if (iVar1 == 0) {
                      return 0xfffffff8;
                    }
                    puVar2 = (undefined8 *)&DAT_ffff00009086dcd8;
                    if (_DAT_ffff00009086dcd8 != (undefined8 *)0x0) {
                      iVar1 = FUN_ffff00009077ede4(*_DAT_ffff00009086dcd8,*param_3);
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
  *(long *)(&DAT_ffff00009086dc08 + lVar3 * 0x18) = param_1;
  *(undefined8 *)(&DAT_ffff00009086dc10 + lVar3 * 0x18) = param_2;
  return 0;
}


