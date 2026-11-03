// File offset entry: 0x9174

void FUN_ffff0000b8009174(void)

{
  undefined8 uVar1;
  undefined8 uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long *plVar7;
  undefined8 *puVar8;
  undefined8 uVar9;
  long alStack_50 [2];
  long lStack_28;
  
  lStack_28 = 0x6b6c;
  uVar1 = *(undefined8 *)PTR_DAT_ffff0000b81086b0;
  uVar2 = *(undefined8 *)(PTR_DAT_ffff0000b81086b0 + 8);
  uVar9 = *(undefined8 *)(PTR_DAT_ffff0000b81086b0 + 0x10);
  iVar3 = FUN_ffff0000b800a01c();
  plVar7 = (long *)FUN_ffff0000b800db20(s_BL33_reserved_ffff0000b809d396,0);
  if (plVar7 == (long *)0x0) {
    FUN_ffff0000b804c33c(s_reserved_memory_BL33_reserved_do_ffff0000b808898c);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830(s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0xad,&DAT_ffff0000b8086efb);
  }
  alStack_50[0] = *plVar7 + -0x1000000000000;
  iVar4 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_BL33_reserved_ffff0000b8095164,plVar7[1],
                     alStack_50,0xc,*plVar7,1,0);
  if (iVar4 != 0) {
    FUN_ffff0000b804c33c(s_BL33_vmm_alloc_physical_failed__e_ffff0000b809f264,iVar4);
LAB_ffff0000b8009274:
    FUN_ffff0000b800d264(*plVar7);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0xff,
               s_app_load_bl33______NO_ERROR_ffff0000b8086e8f);
  }
  iVar4 = FUN_ffff0000b8050eb8(&lStack_28,&DAT_ffff0000b809c16c,alStack_50[0],plVar7[1]);
  if (iVar4 < 0) {
    FUN_ffff0000b804c33c(s_BL33_load_failed__ret___d_ffff0000b809f28d,iVar4);
    FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,alStack_50[0]);
    goto LAB_ffff0000b8009274;
  }
  iVar4 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_bl33_ffff0000b80998e0,
             iVar4 - iVar3);
  alStack_50[0] = 0x656574;
  alStack_50[1] = 0;
  iVar3 = FUN_ffff0000b800a01c();
  plVar7 = (long *)FUN_ffff0000b800db20(s_BL31_reserved_ffff0000b809f256,0);
  if (plVar7 == (long *)0x0) {
    FUN_ffff0000b804c33c(s_reserved_memory_BL31_reserved_do_ffff0000b8098a4a);
    plVar7 = (long *)FUN_ffff0000b804b7b8
                               (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x75,&DAT_ffff0000b8086efb,
                                s_LK_ATF_ffff0000b809e218);
  }
  lStack_28 = *plVar7 + -0x1000000000000;
  iVar4 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_BL31_reserved_ffff0000b8085f8c,plVar7[1],
                     &lStack_28,0xc,*plVar7,1,0);
  if (iVar4 == 0) {
    iVar4 = FUN_ffff0000b8050eb8(alStack_50,&DAT_ffff0000b809e21f,lStack_28,plVar7[1]);
    if (iVar4 < 0) {
      FUN_ffff0000b804c33c(s__BL31__load_failed__ret___d_ffff0000b8092318,iVar4);
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lStack_28);
      goto LAB_ffff0000b8009390;
    }
  }
  else {
    FUN_ffff0000b804c33c(s__BL31__vmm_alloc_physical_failed_ffff0000b808f803,iVar4);
LAB_ffff0000b8009390:
    FUN_ffff0000b800d264(*plVar7);
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x100,
               s_app_load_bl31______NO_ERROR_ffff0000b80a0dd4,s_LK_ATF_ffff0000b809e218);
  }
  FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lStack_28);
  iVar4 = FUN_ffff0000b8028f30();
  if (iVar4 != 0) {
    FUN_ffff0000b804c33c(s__BL31__Prepare_bl31_dtb_failed_r_ffff0000b80a0e15,iVar4);
  }
  iVar4 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c
            (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_bl31_ffff0000b80998ea,
             iVar4 - iVar3);
  iVar3 = FUN_ffff0000b8017444();
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b800e398();
    FUN_ffff0000b80174d8();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_pvmfw_ffff0000b808f82e,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b80095dc;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b8026b44();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_sspm_ffff0000b8087e21,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b80095fc;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b801a8bc();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_kraken_ffff0000b809f2a8,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b800961c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b801d47c();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_mcupm_ffff0000b808e84a,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b800963c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b8029108();
    iVar5 = FUN_ffff0000b8027fd8();
    iVar6 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_bl32_ffff0000b809c16f,
               iVar6 - iVar3);
    if (iVar5 != 0 || iVar4 != 0) goto LAB_ffff0000b800965c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b800e3d0();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_picachu_ffff0000b8086efd,
               iVar5 - iVar3);
    if (iVar4 == 0) {
      iVar3 = FUN_ffff0000b800a01c();
      iVar4 = FUN_ffff0000b800e388();
      iVar5 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804c33c
                (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_ise_ffff0000b809c179,
                 iVar5 - iVar3);
      if (iVar4 != 0) goto LAB_ffff0000b800969c;
      puVar8 = (undefined8 *)FUN_ffff0000b800db20(s_BL31_reserved_ffff0000b809f256,0);
      if (puVar8 == (undefined8 *)0x0) {
        FUN_ffff0000b804c33c(s_reserved_memory_BL31_reserved_do_ffff0000b8098a4a);
        puVar8 = (undefined8 *)
                 FUN_ffff0000b804b7b8
                           (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x10d,&DAT_ffff0000b8086efb,
                            s_LK_ATF_ffff0000b809e218);
      }
      FUN_ffff0000b800e6c0(0xffff0000b8009158,*puVar8,uVar1,uVar2,uVar9);
      goto LAB_ffff0000b80095bc;
    }
  }
  else {
LAB_ffff0000b80095bc:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x101,
               s_app_load_hypervisor______NO_ERRO_ffff0000b80a007e,s_LK_GZ_ffff0000b8097d0d);
LAB_ffff0000b80095dc:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x102,
               s_app_load_pvmfw______NO_ERROR_ffff0000b8086eab,s_LK_PVMFW_ffff0000b8086ec8);
LAB_ffff0000b80095fc:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x103,
               s_app_load_sspm______NO_ERROR_ffff0000b8088969,s_LK_SSPM_ffff0000b809f24e);
LAB_ffff0000b800961c:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x104,
               s_app_load_kraken______NO_ERROR_ffff0000b80914d9,s_LK_KRAKEN_ffff0000b80a0df0);
LAB_ffff0000b800963c:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x105,
               s_app_load_mcupm______NO_ERROR_ffff0000b80940d2,s_LK_MCUPM_ffff0000b80914f7);
LAB_ffff0000b800965c:
    FUN_ffff0000b804b7b8
              (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x106,
               s_app_load_bl32______NO_ERROR_ffff0000b809b50d,s_LK_TEE_ffff0000b809b529);
  }
  FUN_ffff0000b804b7b8
            (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x107,
             s_app_load_picachu______NO_ERROR_ffff0000b8086ed1,s_LK_PICACHU_ffff0000b8086ef0);
LAB_ffff0000b800969c:
  FUN_ffff0000b804b7b8
            (s_app_bl2_ext_bl2_ext_c_ffff0000b80940bc,0x108,
             s_app_load_ise______NO_ERROR_ffff0000b80a0dfa,s_LK_ISE_ffff0000b8088985);
  return;
}


