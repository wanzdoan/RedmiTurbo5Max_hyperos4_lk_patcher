
void FUN_ffff000090714714(void)

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
  
  FUN_ffff00009071f790();
  lVar9 = FUN_ffff00009077f250();
  cVar2 = FUN_ffff000090710bc0();
  iVar3 = FUN_ffff000090716918();
  if ((cVar2 == '\x02') && (lVar9 == 0)) {
    iVar4 = FUN_ffff000090768854(1);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x387,s_ret_>__0_ffff0000907da66e);
    }
    pcVar14 = s_load_recovery_image_ffff0000907f41d8;
  }
  else {
    iVar4 = FUN_ffff000090768854(0);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x38c,s_ret_>__0_ffff0000907da66e);
    }
    pcVar14 = s_load_boot_image_ffff0000907da677;
  }
  iVar4 = FUN_ffff000090716918();
  FUN_ffff00009077e11c(s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,pcVar14,iVar4 - iVar3);
  cVar2 = FUN_ffff000090710bc0();
  if ((cVar2 != '\x02') && (iVar3 = FUN_ffff000090786b8c(), iVar3 != 0)) {
    FUN_ffff00009077e11c
              (s__s__d__Secure_id_Check_fail__ffff0000907d2690,
               s_boot_linux_from_storage_ffff0000907d26ae,0x394);
    FUN_ffff000090787630();
  }
  FUN_ffff00009077e11c
            (s__s__d__Secure_id_Check_Out__ffff0000907e0bf5,
             s_boot_linux_from_storage_ffff0000907d26ae,0x398);
  FUN_ffff00009070ffe4();
  FUN_ffff000090719078();
  uVar5 = FUN_ffff00009070feec();
  if (2 < uVar5) {
    FUN_ffff000090710040();
    FUN_ffff000090719078();
  }
  iVar3 = FUN_ffff00009070ff88();
  uVar10 = FUN_ffff0000907101e0();
  uVar15 = uVar10 & 0xffffffff;
  lVar9 = FUN_ffff00009071023c();
  iVar4 = FUN_ffff00009071094c();
  uVar6 = FUN_ffff0000907109e4();
  if (iVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3a6,
               s_kernel_target_addr____0_ffff0000907d3fad);
  }
  if ((int)uVar10 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3a7,
               s_ramdisk_target_addr____0_ffff000090801fe9);
  }
  if (lVar9 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3a8,s_ramdisk_addr____0_ffff0000907d8daf);
  }
  local_48 = 0;
  if (iVar4 != 0) goto LAB_ffff00009071485c;
  while( true ) {
    FUN_ffff0000907130d8(iVar3,uVar6);
LAB_ffff00009071485c:
    puVar11 = (ulong *)FUN_ffff00009071a348(s_mb_ramdisk_ffff0000907edbfc,0);
    if (puVar11 == (ulong *)0x0) {
      FUN_ffff00009077e11c
                (s__s__d__reserved_memory_mb_ramdis_ffff0000907dc104,
                 s_boot_linux_from_storage_ffff0000907d26ae,0x3b4);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3b5,&DAT_ffff0000907d2506);
    }
    uVar1 = *puVar11;
    uVar16 = (ulong)(iVar4 + 0x800);
    uVar10 = uVar1 - 0x1000000000000;
    local_48 = uVar10;
    if (puVar11[1] < uVar16) {
      FUN_ffff00009077e11c
                (s__s__d__ramdisk_map_sz__0x_x__can_ffff0000907f0e32,
                 s_boot_linux_from_storage_ffff0000907d26ae,0x3bb,uVar16);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3bc,&DAT_ffff0000907d2506);
    }
    if (uVar1 != (uVar15 & 0xffffffff)) {
      FUN_ffff00009077e11c
                (s__s__d__ramdisk_addr__0x_x__is_no_ffff0000907d0b75,
                 s_boot_linux_from_storage_ffff0000907d26ae,0x3c1,uVar15 & 0xffffffff,uVar1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x3c2,&DAT_ffff0000907d2506);
    }
    lVar9 = FUN_ffff00009077b334(uVar10);
    if ((lVar9 == 0) &&
       (iVar7 = FUN_ffff00009077ba64
                          (PTR_DAT_ffff000090839928,s_vm_ramdisk_ffff0000907dda65,
                           uVar16 + 0xfff & 0x1fffff000,&local_48,0xc,uVar1,1,0), uVar10 = local_48,
       iVar7 != 0)) break;
    uVar15 = *puVar11;
    if (uVar10 != uVar15 - 0x1000000000000) {
      iVar7 = 0;
LAB_ffff000090714a94:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0
                (s_err__d__vm_ramdisk__0x_p__expect_ffff0000908005be,iVar7,uVar10,
                 uVar15 - 0x1000000000000);
    }
    uVar5 = FUN_ffff00009070feec();
    uVar15 = local_48;
    if (2 < uVar5) {
      uVar12 = FUN_ffff00009071039c(1);
      uVar8 = FUN_ffff0000907105e8(1);
      FUN_ffff00009077eb20(uVar15,uVar12,uVar8);
      uVar15 = FUN_ffff0000907105e8(1);
      local_48 = local_48 + (uVar15 & 0xffffffff);
      cVar2 = FUN_ffff000090710bc0();
      if (cVar2 == '\x02') {
        lVar9 = FUN_ffff00009071039c(2);
        lVar13 = FUN_ffff000090710310();
        uVar15 = local_48;
        if (lVar9 != lVar13) {
          uVar12 = FUN_ffff00009071039c(2);
          uVar8 = FUN_ffff0000907105e8(2);
          FUN_ffff00009077eb20(uVar15,uVar12,uVar8);
          uVar15 = FUN_ffff0000907105e8(2);
          local_48 = local_48 + (uVar15 & 0xffffffff);
        }
      }
    }
    uVar15 = local_48;
    uVar12 = FUN_ffff00009071023c();
    uVar8 = FUN_ffff000090710818();
    FUN_ffff00009077eb20(uVar15,uVar12,uVar8);
    uVar5 = FUN_ffff00009070feec();
    if (3 < uVar5) {
      uVar15 = FUN_ffff000090710650();
      uVar8 = FUN_ffff000090710798();
      FUN_ffff000090711b4c(uVar15,uVar8);
    }
  }
  uVar15 = *puVar11;
  goto LAB_ffff000090714a94;
}


