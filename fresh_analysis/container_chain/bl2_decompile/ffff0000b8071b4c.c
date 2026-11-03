
ulong FUN_ffff0000b8071b4c(long param_1)

{
  uint uVar1;
  ulong uVar2;
  
  if (param_1 == 0) {
    FUN_ffff0000b804c33c
              (s__s_Wrong_address_in__s_ffff0000b808c13e,s__DICE__ffff0000b8099de7,
               s_vm_unmap_ffff0000b80a0c0b);
    uVar1 = 6;
  }
  else {
    uVar2 = FUN_ffff0000b804a7a4(&DAT_ffff0000b8129bd0,param_1);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff0000b804c33c
              (s__s_VM_unmapping_failed__err___d_ffff0000b808885e,s__DICE__ffff0000b8099de7,
               uVar2 & 0xffffffff);
    uVar1 = 0xd010;
  }
  return (ulong)(uVar1 | 0x100000);
}


