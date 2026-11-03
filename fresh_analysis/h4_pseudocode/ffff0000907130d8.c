
void FUN_ffff0000907130d8(ulong param_1,long param_2,undefined8 param_3,long param_4,uint param_5)

{
  ulong uVar1;
  char cVar2;
  undefined1 uVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  undefined4 uVar7;
  int iVar8;
  int iVar9;
  long lVar10;
  long lVar11;
  long *plVar12;
  long lVar13;
  ulong uVar14;
  uint *puVar15;
  ulong *puVar16;
  undefined8 *puVar17;
  long lVar18;
  ulong uVar19;
  uint *puVar20;
  uint *puVar21;
  uint *local_90;
  undefined4 local_84;
  long local_80;
  long local_78;
  long local_70;
  uint local_64;
  
  local_78 = 0;
  local_70 = 0;
  local_80 = 0;
  local_84 = 0;
  local_90 = (uint *)0x0;
  lVar10 = FUN_ffff00009071009c();
  uVar4 = FUN_ffff00009071019c();
  uVar19 = (ulong)uVar4;
  FUN_ffff00009077e11c
            (s__s__d__kernel_sz_0x_08x_ffff0000907feae3,s_boot_linux_fdt_ffff0000907f0e23,0x225,
             uVar19);
  iVar5 = FUN_ffff00009070feec();
  if (iVar5 - 3U < 2) {
    lVar11 = FUN_ffff000090710310();
    iVar5 = FUN_ffff000090710728();
    lVar18 = 0;
    puVar21 = (uint *)(lVar11 + iVar5);
    uVar6 = FUN_ffff00009070fe8c();
  }
  else {
    if (iVar5 != 2) {
      FUN_ffff00009077e11c
                (s__s__d__64_bits_kernel_ffff0000907e8e3d,s_boot_linux_fdt_ffff0000907f0e23,0x24f);
      if ((param_1 & 0x7ffff) != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cee0(s_64_bit_kernel_can_t_boot_at_0x_0_ffff0000907e75b5,param_1);
      }
      puVar20 = (uint *)(lVar10 + uVar19 + -4);
      iVar5 = 0xbfffc;
      puVar21 = puVar20;
      do {
        if ((((*(char *)((long)puVar20 + 3) == -0x13) && (*(char *)((long)puVar20 + 2) == -2)) &&
            (*(char *)((long)puVar20 + 1) == '\r')) && ((char)*puVar20 == -0x30))
        goto LAB_ffff0000907132a8;
        iVar5 = iVar5 + -1;
        puVar20 = (uint *)((long)puVar20 + -1);
        puVar21 = (uint *)((long)puVar21 + -1);
      } while (iVar5 != 0);
      FUN_ffff00009077e11c
                (s__s__d__Can_t_find_main_device_tr_ffff0000907d8d35,
                 s_boot_linux_fdt_ffff0000907f0e23,0x263);
      puVar21 = (uint *)0x0;
      puVar20 = (uint *)(lVar10 + uVar19 + -0xc0000);
LAB_ffff0000907132a8:
      uVar6 = (puVar20[1] & 0xff00ff00) >> 8 | (puVar20[1] & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar19 = (ulong)(uVar4 - uVar6);
      FUN_ffff00009077e11c
                (s__s__d__dtb_addr_0x_08lx__dtb_siz_ffff0000907d7501,
                 s_boot_linux_fdt_ffff0000907f0e23,0x27a,puVar21,uVar6);
      FUN_ffff00009077e11c
                (s__s__d__kernel_load_addr_0x_08lx__ffff0000907da626,
                 s_boot_linux_fdt_ffff0000907f0e23,0x27b,lVar10,uVar19);
      lVar18 = 0;
      goto LAB_ffff0000907132f8;
    }
    iVar5 = FUN_ffff000090710128();
    iVar9 = FUN_ffff000090710898();
    iVar8 = FUN_ffff00009070ff30();
    puVar21 = (uint *)(lVar10 + iVar5 + (long)iVar9 + (long)iVar8);
    iVar5 = FUN_ffff000090710128();
    iVar9 = FUN_ffff000090710898();
    lVar18 = lVar10 + iVar5 + (long)iVar9;
    uVar6 = FUN_ffff00009070fe8c();
  }
  if (uVar6 == 0) {
    uVar7 = FUN_ffff00009070feec();
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_can_t_find_v_d_dtb_ffff0000907e432d,uVar7);
  }
  uVar4 = (*puVar21 & 0xff00ff00) >> 8 | (*puVar21 & 0xff00ff) << 8;
  if ((uVar4 >> 0x10 | uVar4 << 0x10) == 0xd7b7ab1e) {
    uVar4 = (puVar21[4] & 0xff00ff00) >> 8 | (puVar21[4] & 0xff00ff) << 8;
    uVar4 = (uVar4 >> 0x10 | uVar4 << 0x10) * 0x20 + 0x20;
    puVar21 = (uint *)((long)puVar21 + (ulong)uVar4);
    uVar6 = uVar6 - uVar4;
  }
LAB_ffff0000907132f8:
  plVar12 = (long *)FUN_ffff00009071a348(s_mb_dtb_ffff0000907ebfc3,0);
  if (plVar12 == (long *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__reserved_memory_mb_dtb_do_ffff0000907e0bc7,s_boot_linux_fdt_ffff0000907f0e23
               ,0x281);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x282,&DAT_ffff0000907d2506);
  }
  lVar11 = *plVar12;
  uVar1 = plVar12[1];
  local_70 = lVar11 + -0x1000000000000;
  if (lVar11 != param_2) {
    FUN_ffff00009077e11c
              (s__s__d__fdt__0x_p__is_not_taken_f_ffff0000907dd9f3,s_boot_linux_fdt_ffff0000907f0e23
               ,0x289,param_2,lVar11);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x28a,&DAT_ffff0000907d2506);
  }
  lVar13 = FUN_ffff00009077b334();
  if ((lVar13 == 0) &&
     (iVar5 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_vm_dtb_ffff000090801fe2,uVar1,&local_70,0xc,
                         lVar11,1,0), iVar5 != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_err__d__vm_dtb__0x_p__ffff0000907d265f,iVar5,local_70);
  }
  local_90 = puVar21;
  uVar14 = FUN_ffff00009071fb70();
  if (((uVar14 & 1) == 0) && (cVar2 = FUN_ffff000090710bc0(), cVar2 == '\x02')) {
    iVar5 = FUN_ffff000090785294(lVar18,0,&local_80,&local_84);
  }
  else {
    iVar5 = FUN_ffff000090785484(&DAT_ffff0000907f265c,&DAT_ffff0000907f265c,0,&local_80,&local_84);
  }
  FUN_ffff00009077e11c(s_dtbo_size__u_ffff0000907e5c49,local_84);
  if (iVar5 == 0) {
    iVar5 = FUN_ffff000090785140(puVar21,uVar6,local_80,local_84,&local_90);
    if (iVar5 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x2b0,s_err____NO_ERROR_ffff0000907edbec);
    }
  }
  else {
    uVar4 = (local_90[1] & 0xff00ff00) >> 8 | (local_90[1] & 0xff00ff) << 8;
    FUN_ffff00009077e11c
              (s_load_dtbo_fail__fdt_size__d__ffff0000907e75da,uVar4 >> 0x10 | uVar4 << 0x10);
  }
  uVar4 = (local_90[1] & 0xff00ff00) >> 8 | (local_90[1] & 0xff00ff) << 8;
  puVar15 = (uint *)FUN_ffff000090722088(local_90,uVar4 >> 0x10 | uVar4 << 0x10,1);
  puVar20 = local_90;
  if ((puVar15 != (uint *)0x0) && (puVar20 = puVar15, local_90 != puVar21)) {
    thunk_FUN_ffff0000907c314c();
  }
  local_90 = puVar20;
  iVar5 = FUN_ffff00009076ec6c(local_90,local_70,uVar1 & 0xffffffff);
  if (iVar5 == 0) {
    uVar4 = (*(uint *)(local_70 + 4) & 0xff00ff00) >> 8 | (*(uint *)(local_70 + 4) & 0xff00ff) << 8;
    FUN_ffff00009077e11c
              (s_DT_overlay_ok__fdt_size__d__ffff0000907dda24,uVar4 >> 0x10 | uVar4 << 0x10);
  }
  if (local_90 != puVar21) {
    thunk_FUN_ffff0000907c314c();
  }
  if (local_80 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  FUN_ffff000090712658(local_70);
  puVar16 = (ulong *)FUN_ffff00009071a348(s_mb_kernel_ffff0000907e4341,0);
  if (puVar16 == (ulong *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__reserved_memory_mb_kernel_ffff0000907fb1eb,s_boot_linux_fdt_ffff0000907f0e23
               ,0x2d0);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x2d1,&DAT_ffff0000907d2506);
  }
  uVar1 = puVar16[1];
  local_78 = *puVar16 - 0x1000000000000;
  if (*puVar16 != param_1) {
    FUN_ffff00009077e11c
              (s__s__d__kernel_addr__0x_p__is_not_ffff000090800585,s_boot_linux_fdt_ffff0000907f0e23
               ,0x2d8,param_1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x2d9,&DAT_ffff0000907d2506);
  }
  lVar18 = FUN_ffff00009077b334();
  if ((lVar18 == 0) &&
     (iVar5 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_vm_kernel_ffff0000907df4d8,uVar1,&local_78,0xc,
                         param_1,1,0), iVar5 != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_err__d__vm_kernel__0x_p__ffff0000907d2676,iVar5,local_78);
  }
  FUN_ffff00009077e11c
            (s__s__d__decompress_kernel_image___ffff0000907f2748,s_boot_linux_fdt_ffff0000907f0e23,
             0x2ef);
  iVar5 = FUN_ffff000090712068(lVar10,local_78,uVar19,uVar1 & 0xffffffff);
  if (iVar5 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_decompress_kernel_image_fail____ffff0000907d589b);
  }
  iVar5 = FUN_ffff0000907c5d48();
  if (iVar5 != 0) {
    FUN_ffff00009077e11c(s__AVB__Failed_to_free_AVB_heap__e_ffff0000907e5c57,iVar5);
  }
  FUN_ffff000090719078(s_initcall_blacklist_ufshcd_core_i_ffff0000907dda41);
  FUN_ffff000090713924();
  FUN_ffff000090713a14();
  FUN_ffff000090710ff4();
  FUN_ffff000090711560();
  FUN_ffff0000907117e0();
  FUN_ffff000090711464();
  FUN_ffff000090712620();
  FUN_ffff000090785590(local_70);
  puVar17 = (undefined8 *)FUN_ffff00009071a348(s_BL33_reserved_ffff0000907fb21c,0);
  if (puVar17 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__reserved_memory_BL33_rese_ffff0000907fcc1d,s_boot_linux_fdt_ffff0000907f0e23
               ,0x313);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x314,&DAT_ffff0000907d2506);
  }
  FUN_ffff00009071a7f0(*puVar17);
  FUN_ffff00009071a7f0(param_1);
  FUN_ffff00009071a7f0(lVar11);
  if (param_5 != 0) {
    FUN_ffff00009071a7f0(param_4);
  }
  FUN_ffff000090783fd0();
  FUN_ffff0000907883ac();
  FUN_ffff00009071b224(local_70);
  iVar5 = FUN_ffff000090718b90(local_70);
  if (iVar5 == 0) {
    iVar5 = FUN_ffff00009076da24(local_70,s__chosen_ffff0000907ea70b);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x337,s_offset_>__0_ffff0000907f4116);
    }
    uVar6 = (uint)param_4;
    uVar4 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
    local_64 = uVar4 >> 0x10 | uVar4 << 0x10;
    iVar9 = FUN_ffff00009076e6d4(local_70,iVar5,s_linux_initrd_start_ffff0000907da65b,&local_64,4);
    if (iVar9 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x33a,s_err_>__0_ffff0000907ebfca);
    }
    iVar9 = FUN_ffff000090711be4(local_70,param_4 + (ulong)param_5 + -0x1000000000000);
    uVar4 = uVar6 + param_5 + iVar9;
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    local_64 = uVar4 >> 0x10 | uVar4 << 0x10;
    iVar5 = FUN_ffff00009076e6d4(local_70,iVar5,s_linux_initrd_end_ffff0000907d8d5a,&local_64,4);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x343,s_err_>__0_ffff0000907ebfca);
    }
    uVar3 = FUN_ffff000090710bc0();
    iVar5 = FUN_ffff000090788bf8(uVar3);
    if (iVar5 != 0) {
      FUN_ffff00009077e11c
                (s__s__d___SEC__Failed_to_update_OT_ffff0000907dc0cd,
                 s_boot_linux_fdt_ffff0000907f0e23,0x347,iVar5);
    }
    iVar5 = FUN_ffff000090716918();
    FUN_ffff00009072a4c0(local_70);
    iVar9 = FUN_ffff000090716918();
    FUN_ffff00009077e11c
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_hyp_unmap2_fdt__ffff0000907fb22a,
               iVar9 - iVar5);
    FUN_ffff000090714508(local_70);
    iVar5 = FUN_ffff00009076ef60(local_70);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x357,s_err_>__0_ffff0000907ebfca);
    }
    uVar7 = FUN_ffff000090713910(local_70 + 4);
    FUN_ffff00009077e11c
              (s__s__d__booting_linux___0x_llx__r_ffff0000907d8d6b,s_boot_linux_fdt_ffff0000907f0e23
               ,0x35b,param_1,param_4,param_5,lVar11,uVar7);
    uVar3 = FUN_ffff000090710bc0();
    FUN_ffff00009077e11c
              (s__s__d__lk_boot_mode____d_ffff0000907e75f8,s_boot_linux_fdt_ffff0000907f0e23,0x35c,
               uVar3);
    FUN_ffff00009077e11c
              (s__s__d__lk_finished___>_jump_to_l_ffff0000907f41a7,s_boot_linux_fdt_ffff0000907f0e23
               ,0x35e,s_64Bit_ffff0000907fb23a);
    FUN_ffff000090764728();
    FUN_ffff000090764d7c();
    FUN_ffff00009076528c();
    FUN_ffff00009071ec08(&LAB_ffff000090714700,0xc2000115,param_1,lVar11,0);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0x334,s_err____NO_ERROR_ffff0000907edbec);
}


