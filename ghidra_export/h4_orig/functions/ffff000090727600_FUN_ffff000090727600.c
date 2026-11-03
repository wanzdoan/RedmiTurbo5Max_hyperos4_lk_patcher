// ffff000090727600  size=156  FUN_ffff000090727600

void FUN_ffff000090727600(void)

{
  int iVar1;
  ulong uVar2;
  char *pcVar3;
  undefined8 local_20;
  long local_18;
  
  local_20 = 0x200000;
  local_18 = 0;
  uVar2 = FUN_ffff000090781c0c();
  if ((uVar2 & 1) == 0) {
    local_18 = FUN_ffff0000907198dc
                         (0x200000,0x10000,0xffffffffffffffff,0,0,s_me_gpu_reserved_ffff0000907d77a9
                         );
  }
  else {
    iVar1 = FUN_ffff000090783718(s_me_gpu_reserved_ffff0000907d77a9,&local_18,&local_20);
    if (iVar1 < 0) {
      pcVar3 = s__s__get_reserved_memory_fail_ffff0000907f61eb;
      goto code_r0xffff000090727684;
    }
  }
  if (local_18 != 0) {
    return;
  }
  pcVar3 = s__s__mblock_allocation_failed_ffff0000907d0d73;
  local_18 = 0;
code_r0xffff000090727684:
  FUN_ffff00009077e11c(pcVar3,s__GPU__LDR__ffff0000907cf48c);
  return;
}


