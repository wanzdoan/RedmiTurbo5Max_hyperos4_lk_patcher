
void FUN_ffff0000b8015728(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = FUN_ffff0000b804bccc(0x40);
  iVar1 = FUN_ffff0000b8050eb8(&DAT_ffff0000b8087f4c,s_clinfo_ffff0000b8091681,uVar2,0x40);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff0000b8049ec8(uVar2);
    uVar3 = (*param_1)(uVar2,0x40);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000b804c33c(s_DPM_xfile_dump_failed__ffff0000b808a879);
    }
    FUN_ffff0000b804c33c(s_not_define_DPM_CLINFO_ffff0000b809b60f);
    return;
  }
  FUN_ffff0000b804c33c(s__DPM__load_DPM_image_fail__s_ffff0000b809c350,s_dpmpt_ffff0000b809e348);
  thunk_FUN_ffff0000b8082558(uVar2);
  return;
}


