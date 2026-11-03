// ffff0000907b50c0  size=116  FUN_ffff0000907b50c0

ulong FUN_ffff0000907b50c0(long param_1)

{
  uint uVar1;
  ulong uVar2;
  
  if (param_1 == 0) {
    FUN_ffff00009077de54
              (s__s_Wrong_address_in__s_ffff0000907dbc0f,s__DICE__ffff0000907f547f,
               s_vm_unmap_ffff00009080198a);
    uVar1 = 6;
  }
  else {
    uVar2 = FUN_ffff00009077bb68(&DAT_ffff00009087ce60,param_1);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff00009077de54
              (s__s_VM_unmapping_failed__err___d_ffff0000907d53f3,s__DICE__ffff0000907f547f,
               uVar2 & 0xffffffff);
    uVar1 = 0xd010;
  }
  return (ulong)(uVar1 | 0x100000);
}


