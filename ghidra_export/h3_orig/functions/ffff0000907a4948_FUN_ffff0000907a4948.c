// ffff0000907a4948  size=260  FUN_ffff0000907a4948

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907a4948(void)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long local_18;
  
  local_18 = -0xffff77bf0000;
  if (_DAT_ffff00009090ad48 == 1) {
    uVar2 = 0;
  }
  else {
    lVar3 = FUN_ffff000090719798
                      (0x10000,0x10000,0x88420000,0x88410000,0,s_crypto_hw_ffff0000907e6f2e);
    if (lVar3 != 0x88410000) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18
                (s__s_crypto_addr__0x_lx__is_not_ta_ffff0000907e216c,s__crypto_hw__ffff0000907d8867,
                 0x88410000,lVar3);
    }
    iVar1 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_crypto_hw_ffff0000907e6f38,0x10000,&local_18,0xc,
                       0x88410000,1,0);
    if ((iVar1 != 0) || (local_18 != -0xffff77bf0000)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18
                (s__s_err__d__vm_crypto_hw__0x_p__e_ffff0000907fc6ec,s__crypto_hw__ffff0000907d8867,
                 iVar1,local_18,0x88410000);
    }
    uVar2 = FUN_ffff0000907a4648(0x88410000,0x10000);
    if ((int)uVar2 == 0) {
      _DAT_ffff00009090ad48 = 1;
    }
  }
  return uVar2;
}


