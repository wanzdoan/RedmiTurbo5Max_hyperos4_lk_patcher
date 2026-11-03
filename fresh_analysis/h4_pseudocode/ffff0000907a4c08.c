
undefined8 FUN_ffff0000907a4c08(void)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long local_18;
  
  local_18 = -0xffff77bf0000;
  if (DAT_ffff00009090ae08 == 1) {
    uVar2 = 0;
  }
  else {
    lVar3 = FUN_ffff0000907198dc
                      (0x10000,0x10000,0x88420000,0x88410000,0,s_crypto_hw_ffff0000907e7072);
    if (lVar3 != 0x88410000) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0
                (s__s_crypto_addr__0x_lx__is_not_ta_ffff0000907e223d,s__crypto_hw__ffff0000907d88fd,
                 0x88410000,lVar3);
    }
    iVar1 = FUN_ffff00009077ba64
                      (PTR_DAT_ffff000090839928,s_vm_crypto_hw_ffff0000907e707c,0x10000,&local_18,
                       0xc,0x88410000,1,0);
    if ((iVar1 != 0) || (local_18 != -0xffff77bf0000)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0
                (s__s_err__d__vm_crypto_hw__0x_p__e_ffff0000907fc888,s__crypto_hw__ffff0000907d88fd,
                 iVar1,local_18,0x88410000);
    }
    uVar2 = FUN_ffff0000907a4908(0x88410000,0x10000);
    if ((int)uVar2 == 0) {
      DAT_ffff00009090ae08 = 1;
    }
  }
  return uVar2;
}


