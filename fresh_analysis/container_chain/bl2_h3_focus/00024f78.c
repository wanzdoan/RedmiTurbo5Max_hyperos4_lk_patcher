// File offset entry: 0x24f78

void FUN_ffff0000b8024f78(void)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  
  lVar2 = FUN_ffff0000b800dccc();
  uVar3 = FUN_ffff0000b80170b8();
  if ((uVar3 & 1) == 0) {
    return;
  }
  uVar3 = FUN_ffff0000b800dbfc();
  uVar4 = (uVar3 >> 0x1b & 0x1ffffffff8) + 0xfff & 0x3ffffff000;
  uVar3 = FUN_ffff0000b800dbfc();
  uVar5 = (uVar3 >> 0x12 & 0x3ffffffffff8) + 0xfff & 0x7ffffffff000;
  uVar3 = FUN_ffff0000b800dbfc();
  lVar1 = uVar5 + uVar4 + ((uVar3 >> 9 & 0x7ffffffffffff8) + 0xfff & 0xfffffffffff000);
  FUN_ffff0000b804beb0
            (s__s__d__smmu_pgtable_size_0x_llx__ffff0000b8094441,
             s_get_ptable_size_by_dram_sz_ffff0000b80a0091,0x4c,lVar1,uVar4,uVar5);
  uVar3 = lVar1 * 2 + 0x403fffU & 0x7ffffffffe00000;
  if (uVar3 != 0) {
    lVar2 = FUN_ffff0000b800c9f4
                      (uVar3,0x200000,lVar2 + 0xc0000000,0,0,
                       s_platform_mtksmmu_mpool_ffff0000b809c79a);
    if (lVar2 != 0) {
      FUN_ffff0000b804beb0
                (s__s__d__smmu_mpool_0x_llx_size__l_ffff0000b809998e,
                 s_mtk_smmu_bl2_ext_init_ffff0000b8099978,0x70,lVar2,uVar3);
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_platform_mediatek_common_smmu_sm_ffff0000b809d558,0x6f,
               s_smmu_mpool____0_ffff0000b8097034);
  }
  FUN_ffff0000b804beb0
            (s__s__d__There_is_No_smmu_mpool_si_ffff0000b809e6ea,
             s_mtk_smmu_bl2_ext_init_ffff0000b8099978,0x6b);
  return;
}


