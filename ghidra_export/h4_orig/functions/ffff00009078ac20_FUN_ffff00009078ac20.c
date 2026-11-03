// ffff00009078ac20  size=264  FUN_ffff00009078ac20

void FUN_ffff00009078ac20(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_28;
  
  local_28 = 0;
  iVar1 = FUN_ffff0000907392bc();
  if (iVar1 != 0) {
    iVar1 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_reserve_memory_scp_share_ffff0000907e62dc,0x350000,
                       &local_28,0xc,iVar1,0,0);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907394f4(local_28,0x350000);
      FUN_ffff00009077e11c(s__SCP__memory_dump_size___0x_x_ffff0000907d6dd5,iVar1);
      if (iVar1 - 0x350001U < 0xffcb0002) {
        FUN_ffff00009077e11c(s__SCP__memory_dump_size_ERR__d_ffff0000907e6dcd,iVar1);
      }
      else {
        uVar2 = FUN_ffff00009077b334(local_28);
        (*param_1)(uVar2,iVar1);
      }
      FUN_ffff00009077be30(DAT_ffff000090839928,local_28);
    }
    else {
      FUN_ffff00009077e11c
                (s__SCP___s__allocate_zip_buffer_fa_ffff0000907d036b,
                 s_save_scp_coredump_ffff0000907d8698);
    }
    return;
  }
  FUN_ffff00009077e11c(s__SCP__get_zip_reserve_memory_fai_ffff0000907e874b);
  return;
}


