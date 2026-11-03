
undefined8 FUN_ffff0000b8062284(void)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long local_18;
  
  local_18 = -0xffff77bf0000;
  if (DAT_ffff0000b81b7058 == 1) {
    uVar2 = 0;
  }
  else {
    lVar3 = FUN_ffff0000b800c9f4
                      (0x10000,0x10000,0x88420000,0x88410000,0,s_crypto_hw_ffff0000b809208d);
    if (lVar3 != 0x88410000) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744
                (s__s_crypto_addr__0x_lx__is_not_ta_ffff0000b808f68b,s__crypto_hw__ffff0000b808a502,
                 0x88410000,lVar3);
    }
    iVar1 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,s_vm_crypto_hw_ffff0000b8092097,0x10000,&local_18,
                       0xc,0x88410000,1,0);
    if ((iVar1 != 0) || (local_18 != -0xffff77bf0000)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744
                (s__s_err__d__vm_crypto_hw__0x_p__e_ffff0000b809e01e,s__crypto_hw__ffff0000b808a502,
                 iVar1,local_18,0x88410000);
    }
    uVar2 = FUN_ffff0000b8062004(0x88410000,0x10000);
    if ((int)uVar2 == 0) {
      DAT_ffff0000b81b7058 = 1;
    }
  }
  return uVar2;
}


