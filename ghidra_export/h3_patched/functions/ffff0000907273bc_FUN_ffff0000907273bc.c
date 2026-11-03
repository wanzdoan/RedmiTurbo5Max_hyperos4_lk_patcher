// ffff0000907273bc  size=156  FUN_ffff0000907273bc

void FUN_ffff0000907273bc(void)

{
  int iVar1;
  ulong uVar2;
  char *pcVar3;
  undefined8 local_20;
  long local_18;
  
  local_20 = 0x200000;
  local_18 = 0;
  uVar2 = FUN_ffff000090781944();
  if ((uVar2 & 1) == 0) {
    local_18 = FUN_ffff000090719798
                         (0x200000,0x10000,0xffffffffffffffff,0,0,s_me_gpu_reserved_ffff0000907d7713
                         );
  }
  else {
    iVar1 = FUN_ffff000090783450(s_me_gpu_reserved_ffff0000907d7713,&local_18,&local_20);
    if (iVar1 < 0) {
      pcVar3 = s__s__get_reserved_memory_fail_ffff0000907f604a;
      goto code_r0xffff000090727440;
    }
  }
  if (local_18 != 0) {
    return;
  }
  pcVar3 = s__s__mblock_allocation_failed_ffff0000907d0d36;
  local_18 = 0;
code_r0xffff000090727440:
  FUN_ffff00009077de54(pcVar3,s__GPU__LDR__ffff0000907cf46d);
  return;
}


