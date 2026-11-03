
undefined8 FUN_ffff0000be0765dc(ulong *param_1,long param_2,ulong param_3)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  char *pcVar4;
  ulong uVar5;
  ulong uVar6;
  
  if (param_1 == (ulong *)0x0) {
    pcVar4 = s_md____NULL_ffff0000be0b948b;
  }
  else {
    if (param_2 != 0) {
      if ((uint)param_1[5] < 0x41) {
        if (CARRY8(*param_1,param_3)) {
          uVar3 = 0x19;
        }
        else {
          if (param_3 != 0) {
            do {
              uVar2 = (uint)param_1[5];
              if ((uVar2 == 0) && (uVar6 = param_3 - 0x40, 0x3f < param_3)) {
                FUN_ffff0000be076ba0(param_1,param_2);
                uVar5 = *param_1;
                param_2 = param_2 + 0x40;
LAB_ffff0000be076658:
                *param_1 = uVar5 + 0x200;
              }
              else {
                uVar5 = param_3;
                if (0x40 - uVar2 <= param_3) {
                  uVar5 = (ulong)(0x40 - uVar2);
                }
                FUN_ffff0000be0607b0((long)param_1 + 0x2c + (ulong)uVar2,param_2,uVar5);
                param_2 = param_2 + uVar5;
                uVar6 = param_3 - uVar5;
                iVar1 = (int)param_1[5] + (int)uVar5;
                *(int *)(param_1 + 5) = iVar1;
                if (iVar1 == 0x40) {
                  FUN_ffff0000be076ba0(param_1,(long)param_1 + 0x2c);
                  uVar5 = *param_1;
                  *(undefined4 *)(param_1 + 5) = 0;
                  goto LAB_ffff0000be076658;
                }
              }
              param_3 = uVar6;
            } while (uVar6 != 0);
          }
          uVar3 = 0;
        }
      }
      else {
        uVar3 = 0x10;
      }
      return uVar3;
    }
    pcVar4 = s_in____NULL_ffff0000be0b70a9;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be0758b8(pcVar4,s_platform_mediatek_mt6991_sboot_c_ffff0000be0b820e,0xe5);
}


