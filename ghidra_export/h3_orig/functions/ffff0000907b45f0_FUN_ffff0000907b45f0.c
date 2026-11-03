// ffff0000907b45f0  size=184  FUN_ffff0000907b45f0

long FUN_ffff0000907b45f0(long param_1,long param_2)

{
  int iVar1;
  char *pcVar2;
  long local_18;
  
  local_18 = param_1 + -0x1000000000000;
  if (param_1 == 0) {
    pcVar2 = s__s_Wrong_address_in__s_ffff0000907dbc0f;
  }
  else {
    if (param_2 != 0) {
      iVar1 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_dice_reserved_ffff0000907d7015,param_2,&local_18,
                         0xc,param_1,1,0);
      if (iVar1 == 0) {
        return local_18;
      }
      FUN_ffff00009077de54
                (s__s_VM_mapping_failed__err___d_ffff0000907d893f,s__DICE__ffff0000907f547f,iVar1);
      return 0;
    }
    pcVar2 = s__s_Wrong_size_in__s_ffff00009080336e;
  }
  FUN_ffff00009077de54(pcVar2,s__DICE__ffff0000907f547f,s_vm_map_ffff0000907fff7c);
  return 0;
}


