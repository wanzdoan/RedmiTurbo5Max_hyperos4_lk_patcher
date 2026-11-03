
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be030b3c(void)

{
  int *piVar1;
  ulong uVar2;
  int *piVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  undefined8 uVar7;
  char *pcVar8;
  int iVar9;
  uint uVar10;
  undefined4 *puVar11;
  int iVar12;
  undefined4 *puVar13;
  undefined4 *puVar14;
  int *piVar15;
  int iVar16;
  undefined4 *puVar17;
  undefined4 *local_98;
  int local_90;
  int local_8c;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  int *local_70;
  long local_68;
  
  local_70 = (int *)0x0;
  local_68 = 0;
  local_88 = 0;
  local_80 = 0x6d707373;
  uStack_78 = 0;
  uVar4 = FUN_ffff0000be063c7c();
  if ((uVar4 & 1) == 0) {
    lVar6 = FUN_ffff0000be01c3b4(0x280000,0x10000,0x9fffffff,0,0,s_SSPM_reserved_ffff0000be0a35b4);
    local_68 = lVar6;
    FUN_ffff0000be05fd98(s_sspm__malloc_start_addr_0x_lx__s_ffff0000be0b66d2,lVar6,0x280000);
    iVar5 = FUN_ffff0000be01d37c();
    if (iVar5 != 0) {
      uVar7 = FUN_ffff0000be01c3b4
                        (0x280000,0x10000,0xffffffffffffffff,lVar6 + 0x400000000,0,
                         s_SSPM_reserved_ffff0000be0a35b4);
      FUN_ffff0000be05fd98(s_sspm__mirror_malloc_start_addr_0_ffff0000be0baf97,uVar7,0x280000);
    }
  }
  else {
    FUN_ffff0000be0304f8();
    iVar5 = FUN_ffff0000be065530(s_SSPM_reserved_ffff0000be0a35b4,&local_68,&local_88);
    lVar6 = local_68;
    if (iVar5 < 0) {
      pcVar8 = s_sspm_err__get_reserved_in_fail_ffff0000be0b545b;
      goto LAB_ffff0000be030ca0;
    }
  }
  if (lVar6 == 0) {
    pcVar8 = s_sspm_mblock_allocation_failed_ffff0000be0afcbf;
LAB_ffff0000be030ca0:
    FUN_ffff0000be05fd98(pcVar8);
    return -5;
  }
  iVar5 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_vm_SSPM_reserved_ffff0000be0a9173,0x280000,&local_70
                     ,0xc,lVar6,0,0);
  if (iVar5 != 0) {
    FUN_ffff0000be05fd98(s_sspm_vmm_alloc_physical_failed__e_ffff0000be0b7892,iVar5);
    goto joined_r0xffff0000be030ecc;
  }
  FUN_ffff0000be095cd8();
  piVar3 = local_70;
  piVar1 = local_70 + 6;
  if ((uVar4 & 1) == 0) {
    iVar5 = FUN_ffff0000be066394(&local_80,s_tinysys_sspm_ffff0000be0c645b,piVar1,0x280000);
    if (-1 < iVar5) goto LAB_ffff0000be030ccc;
    FUN_ffff0000be05fd98(s_sspm_load_failed__ret___d_ffff0000be0afcfc,iVar5);
  }
  else {
    iVar5 = 0;
LAB_ffff0000be030ccc:
    piVar15 = piVar1;
    if (*piVar1 == 0x58901690) {
      iVar16 = 0;
      iVar12 = 0;
      puVar13 = (undefined4 *)0x0;
      puVar14 = (undefined4 *)0x0;
      puVar17 = (undefined4 *)0x0;
      local_98 = (undefined4 *)0x0;
      local_90 = 0;
      local_8c = 0;
      do {
        iVar9 = piVar15[4];
        puVar11 = (undefined4 *)((long)piVar15 + (ulong)(uint)piVar15[1]);
        if (iVar9 < 3) {
          if (iVar9 == 0) {
            local_8c = piVar15[2];
            puVar14 = puVar11;
            iVar9 = local_8c;
          }
          else if (iVar9 == 1) {
            iVar12 = piVar15[2];
            puVar17 = puVar11;
            iVar9 = iVar12;
          }
          else {
LAB_ffff0000be030d08:
            FUN_ffff0000be05fd98(s_sspm_warning__Ignore_unknown_ssp_ffff0000be0b547b);
            iVar9 = piVar15[2];
          }
        }
        else if (iVar9 == 3) {
          iVar16 = piVar15[2];
          iVar9 = iVar16;
          local_98 = puVar11;
        }
        else {
          if (iVar9 != 99) goto LAB_ffff0000be030d08;
          local_90 = piVar15[2];
          puVar13 = puVar11;
          iVar9 = local_90;
        }
        piVar15 = (int *)((long)puVar11 + (ulong)((iVar9 + piVar15[3]) - 1U & -piVar15[3]));
      } while (*piVar15 == 0x58901690);
      if (((puVar17 != (undefined4 *)0x0) && (puVar14 != (undefined4 *)0x0)) &&
         (puVar13 != (undefined4 *)0x0)) {
        if (iVar16 == 0) {
          if (0 < iVar12) {
            uVar10 = iVar12 + 4;
            puVar11 = (undefined4 *)&DAT_ffff00001c300000;
            do {
              uVar10 = uVar10 - 4;
              *puVar11 = *puVar17;
              puVar11 = puVar11 + 1;
              puVar17 = puVar17 + 1;
            } while (4 < uVar10);
          }
          FUN_ffff0000be060ae0(piVar3,0,0x18);
          piVar3[2] = 0x200000;
          piVar3[3] = 0x40000 - iVar12;
          *piVar3 = (int)puVar14 - (int)local_70;
          piVar3[1] = local_8c;
          piVar3[4] = ((int)puVar13 - (int)local_70) + -0x18;
          piVar3[5] = local_90 + 0x1c;
          if ((uint)(local_8c + iVar12 + local_90) < 0x1fffb9) {
            if (local_98 != (undefined4 *)0x0) {
              uVar2 = (long)local_70 + 0x27ffff;
              FUN_ffff0000be0607b0(uVar2 & 0xfffffffffffc0000 | 0x3e000,local_98,0);
              _DAT_ffff00001c340094 = (undefined4)(uVar2 >> 0x12);
              FUN_ffff0000be05fd98
                        (s_Re_allocate_CPM_to_0x_x__DDR_RMA_ffff0000be0bbfdc,
                         uVar2 & 0xfffc0000 | 0x3e000,uVar2 >> 0x12 & 0xffffffff);
            }
            FUN_ffff0000be095cb8();
            FUN_ffff0000be095ccc();
            _DAT_ffff00001c340020 = (undefined4)local_68;
            _DAT_ffff00001c340024 = uVar4 & 1;
            FUN_ffff0000be007328(local_70,0x280000);
            FUN_ffff0000be030f8c(local_68,uVar4 & 1);
            _DAT_ffff00001c340000 = 1;
            return 0;
          }
          pcVar8 = s_sspm_error__IMG_is_over_than__dK_ffff0000be0c0ad9;
          iVar16 = 0x800;
        }
        else {
          pcVar8 = s_sspm__cpm___d_bytes__is_oversize_ffff0000be0afd4d;
        }
        FUN_ffff0000be05fd98(pcVar8,iVar16);
        goto LAB_ffff0000be030ebc;
      }
    }
    else {
      puVar14 = (undefined4 *)0x0;
      puVar17 = (undefined4 *)0x0;
    }
    FUN_ffff0000be05fd98(s_sspm_partition_missing___PM_0x_l_ffff0000be0afd17,puVar17,puVar14,piVar1)
    ;
    FUN_ffff0000be05fd98(s_sspm_hdr_id___u__magic__0x_x_ffff0000be0c2e42,piVar15[4],*piVar15);
  }
LAB_ffff0000be030ebc:
  FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_70);
joined_r0xffff0000be030ecc:
  if ((uVar4 & 1) != 0) {
    return iVar5;
  }
  FUN_ffff0000be01c51c(local_68);
  return iVar5;
}


