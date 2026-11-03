
ulong FUN_ffff0000be08f830(long param_1)

{
  uint uVar1;
  ulong uVar2;
  
  if (param_1 == 0) {
    FUN_ffff0000be05fd98
              (s__s_Wrong_address_in__s_ffff0000be0ac16c,s__DICE__ffff0000be0bd9dd,
               s_vm_unmap_ffff0000be0c5c8b);
    uVar1 = 6;
  }
  else {
    uVar2 = FUN_ffff0000be05dd74(&DAT_ffff0000be127118,param_1);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff0000be05fd98
              (s__s_VM_unmapping_failed__err___d_ffff0000be0a7861,s__DICE__ffff0000be0bd9dd,
               uVar2 & 0xffffffff);
    uVar1 = 0xd010;
  }
  return (ulong)(uVar1 | 0x100000);
}


