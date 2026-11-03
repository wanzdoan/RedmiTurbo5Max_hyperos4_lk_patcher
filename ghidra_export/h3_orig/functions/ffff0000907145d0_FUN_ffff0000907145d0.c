// ffff0000907145d0  size=1036  FUN_ffff0000907145d0

void FUN_ffff0000907145d0(void)

{
  ulong uVar1;
  char cVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  undefined4 uVar6;
  int iVar7;
  undefined4 uVar8;
  long lVar9;
  ulong uVar10;
  ulong *puVar11;
  undefined8 uVar12;
  long lVar13;
  char *pcVar14;
  ulong uVar15;
  ulong uVar16;
  ulong local_48;
  
  FUN_ffff00009071f608();
  lVar9 = FUN_ffff00009077ef88();
  cVar2 = FUN_ffff000090710b48();
  iVar3 = FUN_ffff0000907167d4();
  if ((cVar2 == '\x02') && (lVar9 == 0)) {
    iVar4 = FUN_ffff00009076858c(1);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x372,s_ret_>__0_ffff0000907da5a0);
    }
    pcVar14 = s_load_recovery_image_ffff0000907f404c;
  }
  else {
    iVar4 = FUN_ffff00009076858c(0);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x377,s_ret_>__0_ffff0000907da5a0);
    }
    pcVar14 = s_load_boot_image_ffff0000907da5a9;
  }
  iVar4 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54(s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,pcVar14,iVar4 - iVar3);
  cVar2 = FUN_ffff000090710b48();
  if ((cVar2 != '\x02') && (iVar3 = FUN_ffff0000907868c4(), iVar3 != 0)) {
    FUN_ffff00009077de54
              (s__s__d__Secure_id_Check_fail__ffff0000907d2653,
               s_boot_linux_from_storage_ffff0000907d2671,0x37f);
    FUN_ffff000090787370();
  }
  FUN_ffff00009077de54
            (s__s__d__Secure_id_Check_Out__ffff0000907e0b52,
             s_boot_linux_from_storage_ffff0000907d2671,899);
  FUN_ffff00009070ff6c();
  FUN_ffff000090718f34();
  uVar5 = FUN_ffff00009070fe74();
  if (2 < uVar5) {
    FUN_ffff00009070ffc8();
    FUN_ffff000090718f34();
  }
  iVar3 = FUN_ffff00009070ff10();
  uVar10 = FUN_ffff000090710168();
  uVar15 = uVar10 & 0xffffffff;
  lVar9 = FUN_ffff0000907101c4();
  iVar4 = FUN_ffff0000907108d4();
  uVar6 = FUN_ffff00009071096c();
  if (iVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x391,
               s_kernel_target_addr____0_ffff0000907d3f70);
  }
  if ((int)uVar10 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x392,
               s_ramdisk_target_addr____0_ffff000090801e03);
  }
  if (lVar9 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x393,s_ramdisk_addr____0_ffff0000907d8d18);
  }
  local_48 = 0;
  if (iVar4 != 0) goto LAB_ffff000090714718;
  while( true ) {
    FUN_ffff000090712ff4(iVar3,uVar6);
LAB_ffff000090714718:
    puVar11 = (ulong *)FUN_ffff00009071a204(s_mb_ramdisk_ffff0000907eda8b,0);
    if (puVar11 == (ulong *)0x0) {
      FUN_ffff00009077de54
                (s__s__d__reserved_memory_mb_ramdis_ffff0000907dc02e,
                 s_boot_linux_from_storage_ffff0000907d2671,0x39f);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x3a0,&DAT_ffff0000907d24c9);
    }
    uVar1 = *puVar11;
    uVar16 = (ulong)(iVar4 + 0x800);
    uVar10 = uVar1 - 0x1000000000000;
    local_48 = uVar10;
    if (puVar11[1] < uVar16) {
      FUN_ffff00009077de54
                (s__s__d__ramdisk_map_sz__0x_x__can_ffff0000907f0cc1,
                 s_boot_linux_from_storage_ffff0000907d2671,0x3a6,uVar16);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x3a7,&DAT_ffff0000907d24c9);
    }
    if (uVar1 != (uVar15 & 0xffffffff)) {
      FUN_ffff00009077de54
                (s__s__d__ramdisk_addr__0x_x__is_no_ffff0000907d0b5e,
                 s_boot_linux_from_storage_ffff0000907d2671,0x3ac,uVar15 & 0xffffffff,uVar1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x3ad,&DAT_ffff0000907d24c9);
    }
    lVar9 = FUN_ffff00009077b06c(uVar10);
    if ((lVar9 == 0) &&
       (iVar7 = FUN_ffff00009077b79c
                          (DAT_ffff000090839908,s_vm_ramdisk_ffff0000907dd9c2,
                           uVar16 + 0xfff & 0x1fffff000,&local_48,0xc,uVar1,1,0), uVar10 = local_48,
       iVar7 != 0)) break;
    uVar15 = *puVar11;
    if (uVar10 != uVar15 - 0x1000000000000) {
      iVar7 = 0;
LAB_ffff000090714950:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18
                (s_err__d__vm_ramdisk__0x_p__expect_ffff0000908003e2,iVar7,uVar10,
                 uVar15 - 0x1000000000000);
    }
    uVar5 = FUN_ffff00009070fe74();
    uVar15 = local_48;
    if (2 < uVar5) {
      uVar12 = FUN_ffff000090710324(1);
      uVar8 = FUN_ffff000090710570(1);
      FUN_ffff00009077e858(uVar15,uVar12,uVar8);
      uVar15 = FUN_ffff000090710570(1);
      local_48 = local_48 + (uVar15 & 0xffffffff);
      cVar2 = FUN_ffff000090710b48();
      if (cVar2 == '\x02') {
        lVar9 = FUN_ffff000090710324(2);
        lVar13 = FUN_ffff000090710298();
        uVar15 = local_48;
        if (lVar9 != lVar13) {
          uVar12 = FUN_ffff000090710324(2);
          uVar8 = FUN_ffff000090710570(2);
          FUN_ffff00009077e858(uVar15,uVar12,uVar8);
          uVar15 = FUN_ffff000090710570(2);
          local_48 = local_48 + (uVar15 & 0xffffffff);
        }
      }
    }
    uVar15 = local_48;
    uVar12 = FUN_ffff0000907101c4();
    uVar8 = FUN_ffff0000907107a0();
    FUN_ffff00009077e858(uVar15,uVar12,uVar8);
    uVar5 = FUN_ffff00009070fe74();
    if (3 < uVar5) {
      uVar15 = FUN_ffff0000907105d8();
      uVar8 = FUN_ffff000090710720();
      FUN_ffff000090711a68(uVar15,uVar8);
    }
  }
  uVar15 = *puVar11;
  goto LAB_ffff000090714950;
}


