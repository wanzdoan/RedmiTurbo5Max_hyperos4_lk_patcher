// ffff00009072d7b8  size=568  FUN_ffff00009072d7b8

long FUN_ffff00009072d7b8
               (undefined8 param_1,long param_2,uint param_3,int param_4,undefined4 param_5)

{
  long lVar1;
  uint *puVar2;
  undefined8 *puVar3;
  uint uVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  ulong uVar8;
  uint *puVar9;
  int iVar10;
  uint uVar11;
  
  lVar6 = FUN_ffff00009077d24c(param_3 * 0x28);
  if (lVar6 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Alloc_rt_tbl_memor_ffff0000907f60d2,
               s_smem_resv_and_rt_tbl_update_ffff0000907fed97,0x235);
  }
  else {
    FUN_ffff00009077eb88(lVar6,0,param_3 * 0x28);
    lVar7 = FUN_ffff00009072d9f0(param_1,param_5,0);
    if (lVar7 == 0) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__allocate_MD_NC_sha_ffff0000907f102b,
                 s_smem_resv_and_rt_tbl_update_ffff0000907fed97,0x23e);
      thunk_FUN_ffff0000907c2ad8(lVar6);
      lVar6 = 0;
    }
    else {
      if (0 < (int)param_3) {
        iVar10 = 0;
        uVar11 = 0;
        uVar8 = (ulong)param_3;
        puVar9 = (uint *)(lVar6 + 0x14);
        do {
          puVar2 = (uint *)(param_2 + (long)iVar10 * 0x20);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_curr_offset_0x_x____ffff0000907e1012,
                     s_smem_resv_and_rt_tbl_update_ffff0000907fed97,0x247,uVar11,puVar2[1],iVar10);
          uVar4 = *puVar2;
          uVar5 = puVar2[1];
          lVar1 = lVar7 + (ulong)uVar11;
          puVar9[-1] = uVar4;
          *puVar9 = uVar11;
          *(long *)(puVar9 + -5) = lVar1;
          puVar9[4] = uVar11 + param_4;
          if (uVar5 - uVar11 == 0) {
            uVar5 = puVar2[2];
            puVar9[3] = puVar2[4];
            puVar9[1] = uVar5;
            FUN_ffff00009077de54
                      (s__s__d___CCCI_Add_normal_smem___>_ffff0000907d0eed,
                       s_smem_resv_and_rt_tbl_update_ffff0000907fed97,0x256,uVar4,lVar1,uVar11);
            iVar10 = iVar10 + 1;
          }
          else {
            puVar9[3] = 4;
            puVar9[1] = uVar5 - uVar11;
            FUN_ffff00009077de54
                      (s__s__d___CCCI_Add_pading_smem___>_ffff0000907ede94,
                       s_smem_resv_and_rt_tbl_update_ffff0000907fed97,0x251,uVar4,lVar1,uVar11);
          }
          puVar2 = puVar9 + 1;
          puVar9 = puVar9 + 10;
          uVar8 = uVar8 - 1;
          uVar11 = *puVar2 + uVar11;
        } while (uVar8 != 0);
      }
      FUN_ffff00009077de54
                (s__s__d___CCCI_Dump__s_datails_____ffff0000907e29be,
                 s_smem_layout_dump_ffff0000907cf57c,0x32,param_1);
      if (param_3 != 0) {
        lVar7 = 0;
        do {
          puVar3 = (undefined8 *)(lVar6 + lVar7);
          FUN_ffff00009077de54
                    (s__s__d___CCCI___d_0x_llx_0x_08x_0_ffff0000907f105b,
                     s_smem_layout_dump_ffff0000907cf57c,0x37,*(undefined4 *)(puVar3 + 2),*puVar3,
                     *(undefined4 *)((long)puVar3 + 0x14),*(undefined4 *)((long)puVar3 + 0x24),
                     *(undefined4 *)(puVar3 + 3),*(undefined4 *)(puVar3 + 4));
          lVar7 = lVar7 + 0x28;
        } while ((ulong)param_3 * 0x28 - lVar7 != 0);
      }
    }
  }
  return lVar6;
}


