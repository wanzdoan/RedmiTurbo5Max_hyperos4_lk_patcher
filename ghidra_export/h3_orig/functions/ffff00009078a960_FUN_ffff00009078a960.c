// ffff00009078a960  size=264  FUN_ffff00009078a960

void FUN_ffff00009078a960(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_28;
  
  local_28 = 0;
  iVar1 = FUN_ffff000090739024();
  if (iVar1 != 0) {
    iVar1 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_reserve_memory_scp_share_ffff0000907e61c8,0x350000,
                       &local_28,0xc,iVar1,0,0);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009073925c(local_28,0x350000);
      FUN_ffff00009077de54(s__SCP__memory_dump_size___0x_x_ffff0000907d6d3f,iVar1);
      if (iVar1 - 0x350001U < 0xffcb0002) {
        FUN_ffff00009077de54(s__SCP__memory_dump_size_ERR__d_ffff0000907e6c89,iVar1);
      }
      else {
        uVar2 = FUN_ffff00009077b06c(local_28);
        (*param_1)(uVar2,iVar1);
      }
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_28);
    }
    else {
      FUN_ffff00009077de54
                (s__SCP___s__allocate_zip_buffer_fa_ffff0000907d034c,
                 s_save_scp_coredump_ffff0000907d8602);
    }
    return;
  }
  FUN_ffff00009077de54(s__SCP__get_zip_reserve_memory_fai_ffff0000907e8607);
  return;
}


