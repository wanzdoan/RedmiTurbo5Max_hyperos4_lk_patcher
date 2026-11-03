
undefined8 FUN_ffff000090786820(undefined8 *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  
  iVar2 = FUN_ffff0000907861cc();
  uVar1 = iVar2 + 0xffffU & 0xffff0000;
  lVar3 = FUN_ffff0000907198dc
                    (uVar1,0x10000,0xffffffffffffffff,0,0,
                     s_display_others_boot_logo_ext_dec_ffff0000907d38ac);
  param_1[4] = lVar3;
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s_dec_logo_addr_ext_memory_failed_t_ffff0000907fe2f2);
    FUN_ffff00009071a7f0(*param_1);
    FUN_ffff00009077be30(PTR_DAT_ffff000090839928,param_1[2]);
    uVar4 = 0xfffffffb;
  }
  else {
    iVar2 = FUN_ffff00009077ba64
                      (PTR_DAT_ffff000090839928,s_vm_dec_logo_ext_reserved_ffff0000908017e8,uVar1,
                       param_1 + 5,0xc,lVar3,0,0);
    uVar4 = 0;
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_err__d__vm_dec_logo_ext__0x_p__ffff0000907d38d6,iVar2,param_1[5]);
    }
  }
  return uVar4;
}


