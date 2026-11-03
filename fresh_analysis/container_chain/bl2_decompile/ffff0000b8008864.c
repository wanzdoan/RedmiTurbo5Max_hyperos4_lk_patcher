
undefined8
FUN_ffff0000b8008864(long param_1,ulong param_2,undefined8 param_3,uint param_4,uint param_5)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  
  if ((param_2 < *(ulong *)(param_1 + 0x18)) ||
     ((*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1 < param_2)) {
    return 0xffffffdb;
  }
  if ((((uint)param_3 | (uint)param_2) & 0xfff) != 0) {
    return 0xfffffff8;
  }
  if (param_4 == 0) {
    return 0;
  }
  uVar3 = (ulong)param_4;
  lVar2 = uVar3 * 0x1000;
  uVar1 = param_5 & 3;
  if ((*(byte *)(param_1 + 0x10) & 1) == 0) {
    if (uVar1 != 3) {
      uVar5 = *(ulong *)(&DAT_ffff0000b80a2150 + (ulong)uVar1 * 8);
      switch(param_5 >> 2 & 3) {
      case 1:
        uVar5 = uVar5 | 0x40;
        break;
      case 2:
        uVar5 = uVar5 | 0x80;
        break;
      case 3:
        uVar5 = uVar5 | 0xc0;
      }
      if ((param_5 >> 4 & 1) == 0) {
        uVar6 = 0x40000000000000;
        if ((param_5 & 4) != 0) {
          uVar6 = 0x20000000000000;
        }
        uVar6 = uVar6 | (ulong)((param_5 & 2) >> 1) << 0x32;
      }
      else {
        uVar6 = 0x60000000000000;
      }
      uVar5 = uVar5 | param_5 & 0x20 | uVar6;
    }
    else {
      uVar5 = 0xfffffffffffffff8;
    }
    if (param_2 <= uVar3 * -0x1000 + 0x1000000000000) {
      if (*(long *)(param_1 + 8) != 0) {
        uVar4 = FUN_ffff0000b8008300
                          (param_2,param_2,param_3,lVar2,uVar5,0x27,0xc,*(long *)(param_1 + 8),0);
        DataSynchronizationBarrier(3,3,0);
        return uVar4;
      }
LAB_ffff0000b8008ac8:
      FUN_ffff0000b804c33c
                (s__s__d__page_table_is_NULL_ffff0000b8097ccc,s_arm64_mmu_map_ffff0000b80922fa,0x1e9
                );
      return 0xfffffff8;
    }
    uVar4 = 0;
  }
  else {
    if (uVar1 != 3) {
      uVar5 = *(ulong *)(&DAT_ffff0000b80a2150 + (ulong)uVar1 * 8);
      switch(param_5 >> 2 & 3) {
      case 1:
        uVar5 = uVar5 | 0x40;
        break;
      case 2:
        uVar5 = uVar5 | 0x80;
        break;
      case 3:
        uVar5 = uVar5 | 0xc0;
      }
      if ((param_5 >> 4 & 1) == 0) {
        uVar6 = 0x40000000000000;
        if ((param_5 & 4) != 0) {
          uVar6 = 0x20000000000000;
        }
        uVar6 = uVar6 | (ulong)((param_5 & 2) >> 1) << 0x32;
      }
      else {
        uVar6 = 0x60000000000000;
      }
      uVar5 = uVar5 | param_5 & 0x20 | uVar6;
    }
    else {
      uVar5 = 0xfffffffffffffff8;
    }
    if (param_2 + 0x1000000000000 <= uVar3 * -0x1000 + 0x1000000000000) {
      if (*(long *)(param_1 + 8) != 0) {
        uVar4 = FUN_ffff0000b8008300
                          (param_2,param_2 + 0x1000000000000,param_3,lVar2,uVar5,0x27,0xc,
                           *(long *)(param_1 + 8),0xffffffff);
        DataSynchronizationBarrier(3,3,0);
        return uVar4;
      }
      goto LAB_ffff0000b8008ac8;
    }
    uVar4 = 0xffff000000000000;
  }
  FUN_ffff0000b804c33c
            (s__s__d__vaddr_0x_lx__size_0x_lx_o_ffff0000b808c2e1,s_arm64_mmu_map_ffff0000b80922fa,
             0x1e4,param_2,lVar2,uVar4,0x1000000000000);
  return 0xfffffff8;
}


