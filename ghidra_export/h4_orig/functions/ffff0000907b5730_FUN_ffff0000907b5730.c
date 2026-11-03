// ffff0000907b5730  size=116  FUN_ffff0000907b5730

ulong FUN_ffff0000907b5730(long param_1)

{
  uint uVar1;
  ulong uVar2;
  
  if (param_1 == 0) {
    FUN_ffff00009077e11c
              (s__s_Wrong_address_in__s_ffff0000907dbd1a,s__DICE__ffff0000907f55ec,
               s_vm_unmap_ffff000090801b66);
    uVar1 = 6;
  }
  else {
    uVar2 = FUN_ffff00009077be30(&DAT_ffff00009087cef8,param_1);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff00009077e11c
              (s__s_VM_unmapping_failed__err___d_ffff0000907d5430,s__DICE__ffff0000907f55ec,
               uVar2 & 0xffffffff);
    uVar1 = 0xd010;
  }
  return (ulong)(uVar1 | 0x100000);
}


