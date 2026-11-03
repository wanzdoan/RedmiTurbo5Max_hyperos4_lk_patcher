// ffff000090727570  size=144  FUN_ffff000090727570

void FUN_ffff000090727570(code *param_1)

{
  int iVar1;
  char *pcVar2;
  long local_28;
  long local_8;
  
  local_8 = 0;
  local_28 = 0;
  iVar1 = FUN_ffff000090783718(s_me_gpu_reserved_ffff0000907d77a9,&local_8,&local_28);
  if (iVar1 < 0) {
    pcVar2 = s__s__get_reserved_memory_fail_ffff0000907f61eb;
  }
  else if (local_8 == 0) {
    pcVar2 = s__s__mblock_allocation_failed_ffff0000907d0d73;
  }
  else {
    if (local_28 != 0) {
      (*param_1)();
      return;
    }
    pcVar2 = s__s__reserved_memory_size_is_zero_ffff000090800842;
  }
  FUN_ffff00009077e11c(pcVar2,s__GPU__EXPDB__ffff0000907e0fe5);
  return;
}


