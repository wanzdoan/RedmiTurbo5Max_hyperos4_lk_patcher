// ffff0000907310fc  size=456  FUN_ffff0000907310fc

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff0000907310fc(undefined4 *param_1)

{
  uint uVar1;
  uint uVar2;
  undefined8 uVar3;
  undefined8 local_60;
  undefined8 local_58;
  uint local_50;
  undefined8 local_48;
  undefined2 local_40;
  undefined2 local_3e;
  undefined2 local_3c;
  undefined2 local_3a;
  undefined2 uStack_38;
  undefined2 uStack_36;
  undefined1 uStack_34;
  undefined1 uStack_33;
  
  uVar1 = param_1[2];
  uVar2 = param_1[3];
  uStack_38 = 0;
  uStack_36 = 0;
  uStack_34 = 0;
  uStack_33 = 0;
  local_48 = 0;
  local_40 = 0;
  local_3e = 0;
  local_3c = 0;
  local_3a = 0;
  local_60 = 0;
  local_58 = 0;
  local_50 = 0;
  FUN_ffff00009077eb88(&local_48,0x2d,0x15);
  uStack_33 = 0;
  FUN_ffff00009077eb88(&local_60,0x2d,0x13);
  local_50 = local_50 & 0xffffff;
  uVar3 = local_48;
  if ((uVar1 & 1) != 0) {
    local_48 = CONCAT62(local_48._2_6_,0x304d);
    uVar3 = local_48;
  }
  if ((uVar1 >> 1 & 1) != 0) {
    local_48._4_4_ = (undefined4)((ulong)uVar3 >> 0x20);
    local_48._0_4_ = CONCAT22(0x314d,(short)uVar3);
    uVar3 = local_48;
  }
  local_48 = uVar3;
  if ((uVar1 >> 2 & 1) != 0) {
    local_48._6_2_ = (undefined2)((ulong)uVar3 >> 0x30);
    local_48._0_4_ = (undefined4)uVar3;
    local_48._0_6_ = CONCAT24(0x324d,(undefined4)local_48);
  }
  if ((uVar1 >> 3 & 1) != 0) {
    local_48 = CONCAT26(0x334d,(undefined6)local_48);
  }
  if ((uVar1 >> 4 & 1) != 0) {
    local_40 = 0x344d;
  }
  if ((uVar1 >> 5 & 1) != 0) {
    local_3e = 0x354d;
  }
  if ((uVar1 >> 6 & 1) != 0) {
    local_3c = 0x364d;
  }
  if ((uVar1 >> 7 & 1) != 0) {
    local_3a = 0x374d;
  }
  if ((uVar1 >> 8 & 1) != 0) {
    uStack_38 = 0x3044;
  }
  if ((uVar1 >> 9 & 1) != 0) {
    uStack_36 = 0x3144;
  }
  uVar3 = local_60;
  if ((uVar2 & 1) != 0) {
    local_60 = CONCAT62(local_60._2_6_,0x2d57);
    uVar3 = local_60;
  }
  if ((uVar2 >> 1 & 1) != 0) {
    local_60._4_4_ = (undefined4)((ulong)uVar3 >> 0x20);
    local_60._0_4_ = CONCAT22(0x7244,(short)uVar3);
    uVar3 = local_60;
  }
  local_60 = uVar3;
  if ((uVar2 >> 2 & 1) != 0) {
    local_60._6_2_ = (undefined2)((ulong)uVar3 >> 0x30);
    local_60._0_4_ = (undefined4)uVar3;
    local_60._0_6_ = CONCAT24(0x6450,(undefined4)local_60);
  }
  if ((uVar2 >> 3 & 1) != 0) {
    local_60 = CONCAT26(0x6d50,(undefined6)local_60);
  }
  uVar3 = local_58;
  if ((uVar2 >> 4 & 1) != 0) {
    local_58 = CONCAT62(local_58._2_6_,0x7250);
    uVar3 = local_58;
  }
  if ((uVar2 >> 5 & 1) != 0) {
    local_58._4_4_ = (undefined4)((ulong)uVar3 >> 0x20);
    local_58._0_4_ = CONCAT22(0x7344,(short)uVar3);
    uVar3 = local_58;
  }
  local_58 = uVar3;
  if ((uVar2 >> 6 & 1) != 0) {
    local_58._6_2_ = (undefined2)((ulong)uVar3 >> 0x30);
    local_58._0_4_ = (undefined4)uVar3;
    local_58._0_6_ = CONCAT24(0x6444,(undefined4)local_58);
  }
  if ((uVar2 >> 7 & 1) != 0) {
    local_58 = CONCAT26(0x764e,(undefined6)local_58);
  }
  if ((uVar2 >> 8 & 1) != 0) {
    local_50 = CONCAT22(local_50._2_2_,0x704f);
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI___0x_016llx___0x_08_ffff0000907d918b,
             s_md_mem_blk_inf_dump_helper_ffff0000907d9170,0x86,*(undefined8 *)(param_1 + 4),
             *param_1,param_1[1],&local_48,&local_60);
  return;
}


