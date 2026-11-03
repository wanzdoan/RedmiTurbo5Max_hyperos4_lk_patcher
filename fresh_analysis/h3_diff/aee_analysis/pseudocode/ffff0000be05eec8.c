
void FUN_ffff0000be05eec8(undefined4 *param_1,ulong param_2)

{
  undefined1 uVar1;
  ulong uVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  undefined8 local_70;
  undefined8 local_68;
  
  if (param_2 != 0) {
    uVar6 = 0;
    uVar7 = param_2;
    do {
      uVar2 = uVar7 - 0x10;
      if (0xf < uVar7) {
        uVar7 = 0x10;
      }
      uVar4 = uVar7 + 3 >> 2;
      uVar5 = param_2 - uVar6;
      if (uVar4 < 2) {
        uVar4 = 1;
      }
      if (0xf < uVar5) {
        uVar5 = 0x10;
      }
      local_70 = 0;
      local_68 = 0;
      FUN_ffff0000be05fd98(s_0x_08lx__ffff0000be0a98cb,param_1);
      if (uVar5 == 0) {
        uVar4 = 0;
LAB_ffff0000be05eff8:
        FUN_ffff0000be05fd98(s__ffff0000be0b91f9);
        if (((uVar4 != 3) && (FUN_ffff0000be05fd98(s__ffff0000be0b91f9), uVar4 != 2)) &&
           (FUN_ffff0000be05fd98(s__ffff0000be0b91f9), uVar4 != 1)) {
          FUN_ffff0000be05fd98(s__ffff0000be0b91f9);
        }
      }
      else {
        local_70 = CONCAT44(local_70._4_4_,*param_1);
        FUN_ffff0000be05fd98(s__08x_ffff0000be0aac18);
        if (4 < uVar7) {
          local_70 = CONCAT44(param_1[1],(undefined4)local_70);
          FUN_ffff0000be05fd98(s__08x_ffff0000be0aac18);
          if (uVar4 != 2) {
            local_68 = CONCAT44(local_68._4_4_,param_1[2]);
            FUN_ffff0000be05fd98(s__08x_ffff0000be0aac18);
            if (uVar4 != 3) {
              local_68 = CONCAT44(param_1[3],(undefined4)local_68);
              FUN_ffff0000be05fd98(s__08x_ffff0000be0aac18);
            }
          }
        }
        if (uVar4 < 4) goto LAB_ffff0000be05eff8;
      }
      FUN_ffff0000be05fd98(&DAT_ffff0000be0c11ce);
      uVar7 = 0;
      do {
        if (uVar7 < ((ulong)((int)uVar5 + 3) & 0x3c)) {
          uVar1 = *(undefined1 *)((long)&local_70 + uVar7);
          iVar3 = FUN_ffff0000be05f82c(uVar1);
          if (iVar3 == 0) goto LAB_ffff0000be05f05c;
          FUN_ffff0000be05fd98(&DAT_ffff0000be0b1486,uVar1);
        }
        else {
LAB_ffff0000be05f05c:
          FUN_ffff0000be05fd98(&DAT_ffff0000be0c4805);
        }
        uVar7 = uVar7 + 1;
      } while (uVar7 != 0x10);
      FUN_ffff0000be05fd98(&DAT_ffff0000be0b6df2);
      param_1 = param_1 + 4;
      uVar6 = uVar6 + 0x10;
      uVar7 = uVar2;
    } while (uVar6 < param_2);
  }
  return;
}


