
ulong FUN_ffff0000be00a070(void)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  undefined8 local_18;
  
  uVar2 = FUN_ffff0000be01cc78();
  local_18 = 0;
  iVar1 = FUN_ffff0000be064ad8(s_mrdump_mem_size_ffff0000be0b72e7,&local_18,8);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__mrdump_get_env_fail_ffff0000be0a30af,s_mrdump_mem_size_ffff0000be0b72e7,
               0x65,s_mrdump_mem_size_ffff0000be0b72e7);
  }
  iVar1 = FUN_ffff0000be060cb8(&local_18,&DAT_ffff0000be0ae340);
  uVar4 = uVar2;
  if (iVar1 != 0) {
    uVar3 = FUN_ffff0000be05f874(&local_18,0,0);
    FUN_ffff0000be0114f0(s_Memory_dump_size_set_to__uM_ffff0000be0a30cf,uVar3 & 0xffffffff);
    if (uVar3 << 0x14 <= uVar2) {
      uVar4 = uVar3 << 0x14;
    }
    if ((uVar3 >> 6 & 0x3fffffffff) == 0) {
      uVar4 = uVar2;
    }
  }
  return uVar4;
}


