// File offset entry: 0x28aa4

ulong FUN_ffff0000b8028aa4(void)

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
  local_38 = (char  [8])s_spmc_dtb_ffff0000b809f3f9._0_8_;
  iVar2 = FUN_ffff0000b800e3c4();
  if (iVar2 == 0) {
    lVar3 = FUN_ffff0000b800c9f4
                      (0x100000,0x100000,0xc0000000,0,0,s_system_bl31_dtb_ffff0000b808b74d);
  }
  else {
    lVar3 = FUN_ffff0000b800d1b4
                      (0x100000,0x100000,0x240000000,0x340000000,0,0,
                       s_system_bl31_dtb_ffff0000b808b74d);
  }
  if (lVar3 != 0) {
    local_28 = 0;
    iVar2 = FUN_ffff0000b804a16c
                      (PTR_DAT_ffff0000b8107610,s_vm_bl31_dtb_reserved_ffff0000b8098c87,0x100000,
                       &local_28,0xc,lVar3,0,0);
    lVar1 = local_28;
    if (iVar2 == 0) {
      if (local_28 != 0) {
        iVar2 = FUN_ffff0000b8050a2c(&local_2c,local_38,local_28,0x100000);
        if (iVar2 < 0) {
          FUN_ffff0000b804beb0
                    (s___s__load__s_failed__ret___d_ffff0000b809c805,s_TFA_LK_ffff0000b80844b4,
                     local_38,iVar2);
          FUN_ffff0000b800d264(lVar3);
        }
        uVar4 = FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lVar1);
        if ((int)uVar4 == 0) {
          return uVar4;
        }
        FUN_ffff0000b804beb0
                  (s__s_vmm_free_region_failed__err____ffff0000b808ed08,s_TFA_LK_ffff0000b80844b4,
                   uVar4 & 0xffffffff);
        pcVar5 = s__s_Umap__s_failed_ffff0000b80925f2;
        goto LAB_ffff0000b8028c60;
      }
    }
    else {
      FUN_ffff0000b804beb0
                (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,s_TFA_LK_ffff0000b80844b4,iVar2
                );
    }
    FUN_ffff0000b804beb0
              (s___s__map__s__llx_failed__ret___d_ffff0000b80925d0,s_TFA_LK_ffff0000b80844b4,
               s_system_bl31_dtb_ffff0000b808b74d,lVar3,0);
    FUN_ffff0000b800d264(lVar3);
    return 0;
  }
  pcVar5 = s___s__mblock__s_reserve_failed__ffff0000b809f402;
LAB_ffff0000b8028c60:
  FUN_ffff0000b804beb0(pcVar5,s_TFA_LK_ffff0000b80844b4,s_system_bl31_dtb_ffff0000b808b74d);
  return 0xffffffff;
}


