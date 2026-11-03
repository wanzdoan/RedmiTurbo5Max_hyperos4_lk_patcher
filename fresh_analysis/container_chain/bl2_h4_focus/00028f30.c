// File offset entry: 0x28f30

ulong FUN_ffff0000b8028f30(void)

{
  long lVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  char *pcVar5;
  char local_38 [8];
  undefined1 local_30;
  undefined4 local_2c;
  long local_28;
  
  local_30 = 0;
  local_2c = 0x656574;
  local_38 = (char  [8])s_spmc_dtb_ffff0000b80a04c7._0_8_;
  iVar2 = FUN_ffff0000b800e3e0();
  if (iVar2 == 0) {
    lVar3 = FUN_ffff0000b800c9f4
                      (0x100000,0x100000,0xc0000000,0,0,s_system_bl31_dtb_ffff0000b808c793);
  }
  else {
    lVar3 = FUN_ffff0000b800d1b4
                      (0x100000,0x100000,0x240000000,0x340000000,0,0,
                       s_system_bl31_dtb_ffff0000b808c793);
  }
  if (lVar3 != 0) {
    local_28 = 0;
    iVar2 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,s_vm_bl31_dtb_reserved_ffff0000b8099d60,0x100000,
                       &local_28,0xc,lVar3,0,0);
    lVar1 = local_28;
    if (iVar2 == 0) {
      if (local_28 != 0) {
        iVar2 = FUN_ffff0000b8050eb8(&local_2c,local_38,local_28,0x100000);
        if (iVar2 < 0) {
          FUN_ffff0000b804c33c
                    (s___s__load__s_failed__ret___d_ffff0000b809d918,s_TFA_LK_ffff0000b80854e1,
                     local_38,iVar2);
          FUN_ffff0000b800d264(lVar3);
        }
        uVar4 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar1);
        if ((int)uVar4 == 0) {
          return uVar4;
        }
        FUN_ffff0000b804c33c
                  (s__s_vmm_free_region_failed__err____ffff0000b808fd68,s_TFA_LK_ffff0000b80854e1,
                   uVar4 & 0xffffffff);
        pcVar5 = s__s_Umap__s_failed_ffff0000b80936c1;
        goto LAB_ffff0000b80290ec;
      }
    }
    else {
      FUN_ffff0000b804c33c
                (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,s_TFA_LK_ffff0000b80854e1,iVar2
                );
    }
    FUN_ffff0000b804c33c
              (s___s__map__s__llx_failed__ret___d_ffff0000b809369f,s_TFA_LK_ffff0000b80854e1,
               s_system_bl31_dtb_ffff0000b808c793,lVar3,0);
    FUN_ffff0000b800d264(lVar3);
    return 0;
  }
  pcVar5 = s___s__mblock__s_reserve_failed__ffff0000b80a04d0;
LAB_ffff0000b80290ec:
  FUN_ffff0000b804c33c(pcVar5,s_TFA_LK_ffff0000b80854e1,s_system_bl31_dtb_ffff0000b808c793);
  return 0xffffffff;
}


