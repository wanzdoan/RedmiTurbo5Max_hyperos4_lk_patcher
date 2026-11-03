// ffff000090723448  size=160  FUN_ffff000090723448

void FUN_ffff000090723448(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = FUN_ffff00009077d514(0x40);
  iVar1 = FUN_ffff000090785d18(&DAT_ffff0000907d423c,s_clinfo_ffff0000907e5f8d,uVar2,0x40);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff00009077b334(uVar2);
    uVar3 = (*param_1)(uVar2,0x40);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077e11c(s_DPM_xfile_dump_failed__ffff0000907d9038);
    }
    FUN_ffff00009077e11c(s_not_define_DPM_CLINFO_ffff0000907f7f37);
    return;
  }
  FUN_ffff00009077e11c(s__DPM__load_DPM_image_fail__s_ffff0000907f97b4,s_dpmpt_ffff0000907fcfaf);
  thunk_FUN_ffff0000907c314c(uVar2);
  return;
}


