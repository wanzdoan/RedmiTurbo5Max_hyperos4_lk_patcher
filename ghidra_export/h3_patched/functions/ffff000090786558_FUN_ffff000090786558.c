// ffff000090786558  size=208  FUN_ffff000090786558

undefined8 FUN_ffff000090786558(undefined8 *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  
  iVar2 = FUN_ffff000090785f04();
  uVar1 = iVar2 + 0xffffU & 0xffff0000;
  lVar3 = FUN_ffff000090719798
                    (uVar1,0x10000,0xffffffffffffffff,0,0,
                     s_display_others_boot_logo_ext_dec_ffff0000907d386f);
  param_1[4] = lVar3;
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s_dec_logo_addr_ext_memory_failed_t_ffff0000907fe14a);
    FUN_ffff00009071a6ac(*param_1);
    FUN_ffff00009077bb68(DAT_ffff000090839908,param_1[2]);
    uVar4 = 0xfffffffb;
  }
  else {
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_dec_logo_ext_reserved_ffff00009080160c,uVar1,
                       param_1 + 5,0xc,lVar3,0,0);
    uVar4 = 0;
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_err__d__vm_dec_logo_ext__0x_p__ffff0000907d3899,iVar2,param_1[5]);
    }
  }
  return uVar4;
}


