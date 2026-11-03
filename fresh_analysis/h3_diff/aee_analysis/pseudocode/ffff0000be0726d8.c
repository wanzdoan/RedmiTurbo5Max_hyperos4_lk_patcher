
ulong FUN_ffff0000be0726d8(long param_1,ulong param_2,ulong param_3)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  
  if (param_3 == 0) {
    FUN_ffff0000be05fd98(s__UFS__err__invalid_erase_size__l_ffff0000be0c013b,0);
  }
  else {
    uVar5 = *(ulong *)(param_1 + 0x28);
    uVar6 = 0;
    if (uVar5 != 0) {
      uVar6 = param_2 / uVar5;
    }
    if (param_2 == uVar6 * uVar5) {
      uVar6 = 0;
      if (uVar5 != 0) {
        uVar6 = param_3 / uVar5;
      }
      if (param_3 == uVar6 * uVar5) {
        lVar7 = *(long *)(param_1 + 0xb0);
        uVar3 = FUN_ffff0000be06d878(*(undefined4 *)(param_1 + 0xa8));
        uVar6 = (ulong)uVar3;
        if ((int)uVar3 < 0) {
          FUN_ffff0000be05fd98
                    (s__UFS__err__switch_partition_fail_ffff0000be0b3774,
                     *(undefined4 *)(param_1 + 0xa8),uVar6);
          return 0xfffffffffffffff8;
        }
        uVar5 = *(ulong *)(param_1 + 0x28);
        lVar1 = lVar7 + uVar6 * 4;
        iVar4 = 0;
        if (uVar5 != 0) {
          iVar4 = (int)((param_3 + param_2) / uVar5);
        }
        uVar3 = iVar4 - 1;
        if (*(uint *)(lVar1 + 0x260) <= uVar3) {
          FUN_ffff0000be05fd98(s__UFS__err__erase_address_out_of_r_ffff0000be0abec7);
          FUN_ffff0000be05fd98
                    (s__UFS__err__lun__d__blk_cnt__d__s_ffff0000be0bc859,uVar6,
                     *(undefined4 *)(lVar1 + 0x260),param_2,param_3,uVar3);
          return 0xffffffffffffffdb;
        }
        uVar2 = 0;
        if (uVar5 != 0) {
          uVar2 = param_2 / uVar5;
        }
        FUN_ffff0000be01a730(lVar7 + 0x270,0xffffffff);
        iVar4 = (**(code **)(lVar7 + 0x208))(lVar7,uVar6,uVar2 & 0xffffffff,iVar4 - (int)uVar2);
        FUN_ffff0000be01a7c8(lVar7 + 0x270);
        if (iVar4 != 0) {
          FUN_ffff0000be05fd98
                    (s__UFS__err__erase_fail_<0x_llx___0_ffff0000be0ae033,param_2,param_3 + param_2,
                     uVar2 & 0xffffffff,uVar3,iVar4);
          return 0xffffffffffffffec;
        }
        return param_3;
      }
    }
    FUN_ffff0000be05fd98(s__UFS__err__non_aligned_erase_add_ffff0000be0a9ace,param_2,param_3);
  }
  return 0xfffffffffffffff8;
}


