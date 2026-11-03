
ulong FUN_ffff0000be06de04
                (long *param_1,uint param_2,int param_3,undefined4 param_4,undefined4 param_5,
                undefined4 param_6,undefined4 param_7,undefined4 param_8)

{
  ulong uVar1;
  long lVar2;
  long lVar3;
  uint uVar4;
  undefined4 uVar5;
  ulong local_110;
  long local_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined4 local_f0;
  undefined4 uStack_ec;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined4 local_d8;
  undefined4 uStack_d4;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined4 local_c0;
  undefined4 uStack_bc;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined4 local_a8;
  undefined4 uStack_a4;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined4 local_90;
  undefined4 uStack_8c;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined4 local_78;
  undefined4 uStack_74;
  undefined8 local_70;
  
  (*(code *)param_1[0x49])(param_1,0x15b00000,0x1fff);
  (*(code *)param_1[0x49])(param_1,0x15b10000,0xffff);
  (*(code *)param_1[0x49])(param_1,0x15b20000,0x7fff);
  (*(code *)param_1[0x49])(param_1,0x15b30000,0x1fff);
  (*(code *)param_1[0x49])(param_1,0x15b40000,0xffff);
  (*(code *)param_1[0x49])(param_1,0x15b50000,0x7fff);
  if (param_2 - 1 < 5) {
    uVar4 = *(uint *)(&DAT_ffff0000be0d2614 + (long)(int)(param_2 - 1) * 4);
  }
  else {
    uVar4 = 0;
  }
  if (param_3 - 1U < 5) {
    uVar5 = *(undefined4 *)(&DAT_ffff0000be0d2614 + (long)(int)(param_3 - 1U) * 4);
  }
  else {
    uVar5 = 0;
  }
  uVar1 = FUN_ffff0000be071f50(param_1,0,1);
  if (((int)uVar1 == 0) && (uVar1 = FUN_ffff0000be071f50(param_1,1,0), (int)uVar1 == 0)) {
    local_110 = 0x1569000000000002;
    local_108 = (ulong)uVar4 << 0x20;
    local_100 = 0xff00000000;
    uStack_f8 = 0x1584000000000002;
    local_f0 = 0;
    local_e8 = 0xff00000000;
    uStack_e0 = 0x1568000000000002;
    local_d8 = 0;
    local_c0 = 0;
    local_a8 = 0;
    local_d0 = 0xff00000000;
    uStack_c8 = 0x1583000000000002;
    local_90 = 0;
    local_78 = 0;
    local_70 = 0xff00000000;
    local_b8 = 0xff00000000;
    uStack_b0 = 0x1560000000000002;
    local_a0 = 0xff00000000;
    uStack_98 = 0x1580000000000002;
    local_88 = 0xff00000000;
    uStack_80 = 0x156a000000000002;
    uStack_ec = uVar5;
    uStack_d4 = param_4;
    uStack_bc = param_5;
    uStack_a4 = param_7;
    uStack_8c = param_8;
    uStack_74 = param_6;
    uVar1 = FUN_ffff0000be06d968(param_1,&local_110,7);
    if ((int)uVar1 == 0) {
      lVar2 = *param_1;
      local_100 = 0;
      local_110 = 0x1571000000000002;
      uVar4 = (param_2 | param_3 << 4) & 0xff;
      local_108 = (ulong)uVar4 << 0x20;
      if ((*(uint *)(lVar2 + 0x30) >> 3 & 1) == 0) {
        uVar1 = 0xffffffff;
      }
      else {
        uVar1 = 0xffffffffffffff9c;
        param_1[10] = (long)&local_110;
        *(undefined4 *)(lVar2 + 0x94) = 0x15710000;
        *(undefined4 *)(lVar2 + 0x98) = 0;
        *(uint *)(lVar2 + 0x9c) = uVar4;
        *(undefined4 *)(lVar2 + 0x90) = 2;
        do {
          lVar2 = *param_1;
          if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) != 0) goto LAB_ffff0000be06e04c;
          FUN_ffff0000be05ede4(100);
          uVar1 = uVar1 + 100;
        } while (uVar1 < 0x7a0bd);
        lVar2 = *param_1;
        if ((*(uint *)(lVar2 + 0x20) >> 10 & 1) == 0) {
          uVar1 = 0xffffffff;
          param_1[10] = 0;
        }
        else {
LAB_ffff0000be06e04c:
          *(undefined4 *)(lVar2 + 0x20) = 0x400;
          lVar3 = param_1[10];
          if (lVar3 != 0) {
            *(uint *)(lVar3 + 8) = *(uint *)(lVar3 + 8) | *(uint *)(lVar2 + 0x98) & 0xff;
            *(undefined4 *)(lVar3 + 0xc) = *(undefined4 *)(lVar2 + 0x9c);
          }
          uVar1 = 0xffffffffffffff9c;
          param_1[10] = 0;
          do {
            lVar2 = *param_1;
            if ((*(uint *)(lVar2 + 0x20) >> 4 & 1) != 0) goto LAB_ffff0000be06e0c0;
            FUN_ffff0000be05ede4(100);
            uVar1 = uVar1 + 100;
          } while (uVar1 < 0x7a0bd);
          lVar2 = *param_1;
          if ((*(uint *)(lVar2 + 0x20) >> 4 & 1) == 0) {
            FUN_ffff0000be05fd98(s__UFS__err__wait_UIC_POWER_MODE_i_ffff0000be0c6ab7);
            uVar1 = 0xffffffff;
          }
          else {
LAB_ffff0000be06e0c0:
            *(undefined4 *)(lVar2 + 0x20) = 0x10;
            uVar4 = *(uint *)(lVar2 + 0x30) >> 8 & 7;
            if (uVar4 == 1) {
              uVar1 = 0;
            }
            else {
              FUN_ffff0000be05fd98
                        (s__UFS__err__pwr_ctrl_cmd_0x_0x_fa_ffff0000be0ba603,local_110 & 0xffffffff,
                         uVar4);
              if (uVar4 == 0) {
                uVar4 = 0xffffffff;
              }
              uVar1 = (ulong)uVar4;
            }
          }
        }
      }
    }
  }
  return uVar1;
}


