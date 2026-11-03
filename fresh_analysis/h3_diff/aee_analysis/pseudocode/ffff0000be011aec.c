
ulong FUN_ffff0000be011aec(void)

{
  undefined8 *puVar1;
  ulong uVar2;
  
  puVar1 = (undefined8 *)FUN_ffff0000be01cb9c(s_aee_debug_kinfo_ffff0000be0bf3d6,0);
  if (puVar1 == (undefined8 *)0x0) {
    FUN_ffff0000be0115fc(s_query_aee_debug_kinfo_fail_ffff0000be0b98f5);
  }
  else {
    uVar2 = FUN_ffff0000be05d9a8
                      (PTR_DAT_ffff0000be0f5570,s_aee_debug_kinfo_ffff0000be0bf3d6,puVar1[1],
                       &DAT_ffff0000be106928,0,*puVar1,0,0);
    if ((int)uVar2 == 0) {
      return uVar2;
    }
    FUN_ffff0000be0115fc(s_vmm_alloc_physical_for_memmap_fa_ffff0000be0a8cd7,uVar2 & 0xffffffff);
  }
  return 0xffffffff;
}


