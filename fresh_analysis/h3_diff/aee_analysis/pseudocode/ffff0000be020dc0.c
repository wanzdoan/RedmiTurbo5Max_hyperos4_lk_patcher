
void FUN_ffff0000be020dc0(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = FUN_ffff0000be05f1bc(0x40);
  iVar1 = FUN_ffff0000be066394(&DAT_ffff0000be0a6c3e,s_clinfo_ffff0000be0b2dd5,uVar2,0x40);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff0000be05d228(uVar2);
    uVar3 = (*param_1)(uVar2,0x40);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000be05fd98(s_DPM_xfile_dump_failed__ffff0000be0aa2f7);
    }
    FUN_ffff0000be05fd98(s_not_define_DPM_CLINFO_ffff0000be0bf5eb);
    return;
  }
  FUN_ffff0000be05fd98(s__DPM__load_DPM_image_fail__s_ffff0000be0c06c3,s_dpmpt_ffff0000be0c2b08);
  thunk_FUN_ffff0000be09937c(uVar2);
  return;
}


