// ffff000090723204  size=160  FUN_ffff000090723204

void FUN_ffff000090723204(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = FUN_ffff00009077d24c(0x40);
  iVar1 = FUN_ffff000090785a50(&DAT_ffff0000907d41ff,s_clinfo_ffff0000907e5e62,uVar2,0x40);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff00009077b06c(uVar2);
    uVar3 = (*param_1)(uVar2,0x40);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s_DPM_xfile_dump_failed__ffff0000907d8fa1);
    }
    FUN_ffff00009077de54(s_not_define_DPM_CLINFO_ffff0000907f7d9b);
    return;
  }
  FUN_ffff00009077de54(s__DPM__load_DPM_image_fail__s_ffff0000907f9618,s_dpmpt_ffff0000907fce07);
  thunk_FUN_ffff0000907c2ad8(uVar2);
  return;
}


