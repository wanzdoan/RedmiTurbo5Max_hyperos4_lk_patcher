
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be030988(code *param_1)

{
  ulong uVar1;
  undefined8 uVar2;
  char *pcVar3;
  int iVar4;
  
  if (_DAT_ffff00001c340024 == 0) {
    FUN_ffff0000be05fd98(s__SSPM__Can_t_find_coredump_ffff0000be0bf915);
    uVar2 = FUN_ffff0000be05d228(&DAT_ffff00001c300000);
    uVar1 = (*param_1)(uVar2,0x40000);
    if ((uVar1 & 1) != 0) {
      return;
    }
    pcVar3 = s__SSPM__AEE_Sram_Coredump_fail_ffff0000be0b4183;
  }
  else {
    iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
    if (iVar4 == 0) {
      FUN_ffff0000be05ede4(100);
      iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
      if (iVar4 == 0) {
        FUN_ffff0000be05ede4(100);
        iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
        if (iVar4 == 0) {
          FUN_ffff0000be05ede4(100);
          iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
          if (iVar4 == 0) {
            FUN_ffff0000be05ede4(100);
            iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
            if (iVar4 == 0) {
              FUN_ffff0000be05ede4(100);
              iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
              if (iVar4 == 0) {
                FUN_ffff0000be05ede4(100);
                iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
                if (iVar4 == 0) {
                  FUN_ffff0000be05ede4(100);
                  iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
                  if (iVar4 == 0) {
                    FUN_ffff0000be05ede4(100);
                    iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
                    if (iVar4 == 0) {
                      FUN_ffff0000be05ede4(100);
                      iVar4 = _DAT_ffff00001c34011c + _DAT_ffff00001c340120;
                      if (iVar4 == 0) {
                        FUN_ffff0000be05ede4(100);
                        return;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    uVar1 = (*param_1)(_DAT_ffff00001c340130,iVar4);
    if ((uVar1 & 1) != 0) {
      return;
    }
    pcVar3 = s__SSPM__AEE_EXPDB_Coredump_fail_ffff0000be0b8ba1;
  }
  FUN_ffff0000be05fd98(pcVar3);
  return;
}


