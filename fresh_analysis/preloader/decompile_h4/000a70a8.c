// File offset entry: 0xa70a8

ulong FUN_020a6fa8(long param_1)

{
  uint uVar1;
  int *piVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  undefined8 uStack_38;
  undefined4 uStack_30;
  undefined4 uStack_2c;
  long lStack_28;
  
  lStack_28 = *plRam0000000002103280;
  uStack_2c = 0;
  uStack_38 = 0;
  uVar6 = FUN_020a6a40(1);
  if ((int)uVar6 == 0) {
    uStack_30 = 0;
    uVar6 = FUN_020a6c9c(5,&uStack_38,&uStack_2c,&uStack_30,0);
    if ((int)uVar6 == 0) {
      if (iRam000000000212315c == 2) {
        uStack_30 = 0x20f;
      }
      else if (iRam000000000212315c == 1) {
        uStack_30 = 399;
      }
      else {
        if (iRam000000000212315c != 0) {
          uVar6 = 0x10101b;
          goto code_r0x020a711c;
        }
        uStack_30 = 0x10f;
      }
      uVar6 = FUN_020a6c9c(3,&uStack_38,&uStack_2c,&uStack_30,1);
      if ((int)uVar6 == 0) {
        uStack_30 = 0;
        uVar6 = FUN_020a6c9c(0x30,&uStack_38,&uStack_2c,&uStack_30,0);
        if ((int)uVar6 == 0) {
          uStack_30 = 0;
          uVar6 = FUN_020a6c9c(2,&uStack_38,&uStack_2c,&uStack_30,0);
          if (((int)uVar6 == 0) &&
             (uVar6 = FUN_020a6e7c(uStack_38,param_1,0x200,param_1 + 0x200), (int)uVar6 == 0)) {
            FUN_020a0fec(param_1,*(undefined4 *)(param_1 + 0x200));
            uStack_30 = 0;
            uVar6 = FUN_020a6c9c(2,&uStack_38,&uStack_2c,&uStack_30,0);
            if ((int)uVar6 == 0) {
              uVar6 = FUN_020a6e7c(uStack_38,param_1 + 0x204,0x200,(undefined4 *)(param_1 + 0x404));
              if ((int)uVar6 == 0) {
                FUN_020a0fec(param_1 + 0x204,*(undefined4 *)(param_1 + 0x404));
                uVar6 = 0;
              }
            }
          }
        }
      }
    }
  }
code_r0x020a711c:
  if (lRam0000000002100880 == lStack_28) {
    return uVar6;
  }
  uVar3 = FUN_02091090(uVar6);
  uVar4 = FUN_020a7cec();
  FUN_0208ada0(0x20d01dd,uVar4);
  iVar5 = FUN_020a7cec();
  if (iVar5 == 0) {
    iVar5 = FUN_020a0e58(*puRam0000000002103820,0x21240ec,uRam00000000021242ec);
    uVar1 = 0;
    if (iVar5 != 0) {
      uVar1 = 0x101017;
    }
    uVar6 = (ulong)uVar1;
  }
  else {
    FUN_0208ada0(0x20cefb0);
    piVar2 = piRam0000000002103810;
    uVar6 = FUN_0209c58c(0x21244f4,uVar3,0x2123a5c,*piRam0000000002103810);
    if ((int)uVar6 == 0) {
      if (*piVar2 - 1U < 2) {
        uVar6 = func_0x020a7d10(0x2123a9c,0x30,*puRam0000000002103830);
        if ((int)uVar6 != 0) {
          return uVar6;
        }
        uVar7 = 0x30;
      }
      else {
        if (*piVar2 != 0) {
          return 0x10101b;
        }
        uVar6 = func_0x020a7d10(0x2123a9c,0x20,*puRam0000000002103830);
        if ((int)uVar6 != 0) {
          return uVar6;
        }
        uVar7 = 0x20;
      }
      iVar5 = FUN_020a0e58(0x2123a5c,0x2123a9c,uVar7);
      uVar1 = 0;
      if (iVar5 != 0) {
        uVar1 = 0x101017;
      }
      uVar6 = (ulong)uVar1;
    }
  }
  return uVar6;
}


