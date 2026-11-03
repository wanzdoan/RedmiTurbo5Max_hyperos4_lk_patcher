
undefined8
FUN_ffff0000be008070
          (ulong param_1,ulong param_2,ulong param_3,ulong param_4,ulong param_5,ulong param_6,
          ulong param_7,long param_8,uint param_9)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  ulong uVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong local_68;
  
  if (((param_3 | param_2 | param_4) & (-1L << (param_7 & 0x3f) ^ 0xffffffffffffffffU)) == 0) {
    if (param_4 != 0) {
      uVar13 = 1L << (param_6 & 0x3f);
      uVar9 = (uint)param_7;
      uVar3 = 1 << (ulong)(uVar9 & 0x1f);
      uVar8 = (uint)param_6;
      uVar2 = 3;
      if (uVar9 <= uVar8 && uVar8 - uVar9 != 0) {
        uVar2 = 1;
      }
      uVar12 = param_2;
      uVar15 = param_4;
      uVar16 = param_1;
      do {
        uVar14 = uVar12 & uVar13 - 1;
        uVar10 = uVar13 - uVar14;
        uVar1 = uVar15;
        if (uVar10 <= uVar15) {
          uVar1 = uVar10;
        }
        uVar10 = uVar12 >> (param_6 & 0x3f);
        if (((((param_3 | uVar12) & uVar13 - 1) == 0) && (uVar8 < 0x1f)) && (uVar1 == uVar13)) {
          if (*(long *)(param_8 + uVar10 * 8) != 0) {
            FUN_ffff0000be05fd98
                      (s__s__d__page_table_entry_already_i_ffff0000be0b4ee6,
                       s_arm64_mmu_map_pt_ffff0000be0c5eb1,0x1b5,uVar10);
            goto LAB_ffff0000be008354;
          }
          *(ulong *)(param_8 + uVar10 * 8) = uVar2 | param_5 | param_3;
          uVar10 = uVar16 >> 0xc & 0xfffffffffff;
          DataMemoryBarrier(2,2);
          if (param_9 == 0xffffffff) {
            TLBI_VAAE1IS(uVar10);
            InstructionSynchronizationBarrier();
          }
          else {
            TLBI_VAE1IS(uVar10 | (ulong)param_9 << 0x30);
            InstructionSynchronizationBarrier();
          }
        }
        else {
          uVar11 = *(ulong *)(param_8 + uVar10 * 8);
          uVar7 = uVar11 & 3;
          if (uVar7 == 0) {
            local_68 = 0;
            if (uVar9 == 0xc) {
              lVar6 = FUN_ffff0000be05cb80();
              if (lVar6 != 0) {
                local_68 = FUN_ffff0000be05c98c();
LAB_ffff0000be008274:
                lVar6 = FUN_ffff0000be05d1c0(local_68);
                FUN_ffff0000be060ae0(lVar6,0,uVar3);
                DataMemoryBarrier(2,2);
                *(ulong *)(param_8 + uVar10 * 8) = local_68 | 3;
                goto joined_r0xffff0000be0082a0;
              }
            }
            else if (uVar9 < 0xd) {
              lVar6 = FUN_ffff0000be05f204(uVar3,uVar3);
              if (lVar6 != 0) {
                local_68 = FUN_ffff0000be05d228();
                if (local_68 != 0) goto LAB_ffff0000be008274;
                thunk_FUN_ffff0000be09937c(lVar6);
              }
            }
            else {
              uVar7 = FUN_ffff0000be05cfc4((ulong)(uVar3 >> 0xc),param_7 & 0xffffffff,&local_68,0);
              if (uVar7 == uVar3 >> 0xc) goto LAB_ffff0000be008274;
            }
            FUN_ffff0000be05fd98
                      (s__s__d__failed_to_allocate_page_t_ffff0000be0ba9dd,
                       s_arm64_mmu_get_page_table_ffff0000be0a6865,0x115);
LAB_ffff0000be008354:
            FUN_ffff0000be0083a0
                      (param_1,param_2,param_4 - uVar15,param_6,param_7 & 0xffffffff,param_8,
                       (ulong)param_9);
            DataSynchronizationBarrier(3,3,0);
            return 0xffffffff;
          }
          if (uVar7 != 3) {
            if (uVar7 != 1) {
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee24
                        (s__s__d_unimplemented_ffff0000be0c168e,
                         s_pte_t__arm64_mmu_get_page_table__ffff0000be0bf233,0x12d);
            }
            goto LAB_ffff0000be008354;
          }
          lVar6 = FUN_ffff0000be05d1c0(uVar11 & 0xfffffffff000);
joined_r0xffff0000be0082a0:
          if ((lVar6 == 0) ||
             (iVar4 = FUN_ffff0000be008070
                                (uVar16,uVar14,param_3,uVar1,param_5,(uVar8 - uVar9) + 3,param_7,
                                 lVar6,param_9), iVar4 != 0)) goto LAB_ffff0000be008354;
        }
        uVar16 = uVar1 + uVar16;
        uVar12 = uVar1 + uVar12;
        param_3 = uVar1 + param_3;
        uVar15 = uVar15 - uVar1;
      } while (uVar15 != 0);
    }
    uVar5 = 0;
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__not_page_aligned_ffff0000be0c5e98,s_arm64_mmu_map_pt_ffff0000be0c5eb1,0x199)
    ;
    uVar5 = 0xfffffff8;
  }
  return uVar5;
}


