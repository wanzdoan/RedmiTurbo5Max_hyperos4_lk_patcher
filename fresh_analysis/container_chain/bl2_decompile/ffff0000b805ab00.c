
void FUN_ffff0000b805ab00(void)

{
  int iVar1;
  long lVar2;
  undefined8 local_8;
  
  iVar1 = FUN_ffff0000b800e3e0();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000b800c9f4
                      (0x200000,0x200000,0x340000000,0,0,s_me_cmdq_reserved_ffff0000b809efb3);
  }
  else {
    lVar2 = FUN_ffff0000b800d1b4
                      (0x200000,0x200000,0x240000000,0x340000000,0,0,
                       s_me_cmdq_reserved_ffff0000b809efb3);
  }
  if (lVar2 != 0) {
    local_8 = 0;
    FUN_ffff0000b804c33c
              (s__cmdq___s__block__llx_size__llx_l_ffff0000b8097a96,
               s_cmdq_alloc_reserved_mem_ffff0000b80a1741,lVar2,0x200000,0x240000000,0x340000000);
    iVar1 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,s_vm_cmdq_reserved_ffff0000b8092e20,0x200000,
                       &local_8,0xc,lVar2,0,0);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_cmdq_cm_ffff0000b80987b5,0x40,
                 s_ret____NO_ERROR_ffff0000b8091302);
    }
    FUN_ffff0000b804d070(local_8,0,0x200000);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_cmdq_cm_ffff0000b80987b5,0x35,s_block_ffff0000b809a37c);
}


