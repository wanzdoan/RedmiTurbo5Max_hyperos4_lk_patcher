
int FUN_ffff0000b8017150(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  long local_28;
  
  local_28 = 0;
  iVar1 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_bl_reserved_ffff0000b8086150,0x1000,&local_28,0xc
                     ,*(undefined8 *)PTR_DAT_ffff0000b81086b0,0,0);
  if (iVar1 == 0) {
    uVar2 = *(undefined8 *)(local_28 + 0x10);
    *(undefined8 *)(local_28 + 0x10) = param_1;
    DAT_ffff0000b80e8a14 = (undefined4)uVar2;
  }
  else {
    local_28 = 0;
    FUN_ffff0000b804c33c(s__GZ_INIT__CONFIG__mapping_BL_par_ffff0000b808cf23,iVar1);
    if (local_28 == 0) {
      return iVar1;
    }
  }
  FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8);
  return iVar1;
}


