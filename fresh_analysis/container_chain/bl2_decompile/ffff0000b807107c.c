
long FUN_ffff0000b807107c(long param_1,long param_2)

{
  int iVar1;
  char *pcVar2;
  long local_18;
  
  local_18 = param_1 + -0x1000000000000;
  if (param_1 == 0) {
    pcVar2 = s__s_Wrong_address_in__s_ffff0000b808c13e;
  }
  else {
    if (param_2 != 0) {
      iVar1 = FUN_ffff0000b804a5f8
                        (PTR_DAT_ffff0000b81086d8,s_vm_dice_reserved_ffff0000b808978c,param_2,
                         &local_18,0xc,param_1,1,0);
      if (iVar1 == 0) {
        return local_18;
      }
      FUN_ffff0000b804c33c
                (s__s_VM_mapping_failed__err___d_ffff0000b808a594,s__DICE__ffff0000b8099de7,iVar1);
      return 0;
    }
    pcVar2 = s__s_Wrong_size_in__s_ffff0000b80a18f2;
  }
  FUN_ffff0000b804c33c(pcVar2,s__DICE__ffff0000b8099de7,s_vm_map_ffff0000b809ff60);
  return 0;
}


