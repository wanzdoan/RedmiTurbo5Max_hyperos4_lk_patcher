// ffff000090746b64  size=216  FUN_ffff000090746b64

undefined8 FUN_ffff000090746b64(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  undefined4 local_4;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_clkbuf__ffff0000907d9480,100,
               s_fdt____NULL_ffff0000907ff285);
  }
  local_4 = 0;
  iVar1 = FUN_ffff00009076e064(param_1,0,PTR_s_mediatek_mt6991_consys_ffff00009081a6c8);
  if (iVar1 < 0) {
    uVar3 = 2;
  }
  else {
    lVar2 = FUN_ffff00009076dcd4(param_1,iVar1,DAT_ffff00009081a6d0,&local_4);
    if ((lVar2 != 0) && (iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907de35b), iVar1 == 0)) {
      FUN_ffff00009077de54
                (s__CLKBUF___s_support_ffff0000907d7c5a,PTR_s_TCXO_SUPPORT_ffff00009081a6b8);
      (*(code *)PTR_FUN_ffff00009081a6c0)();
      goto LAB_ffff000090746be0;
    }
    uVar3 = 4;
  }
  FUN_ffff00009077de54
            (s__CLKBUF___s_not_support__ret___d_ffff0000907fd679,PTR_s_TCXO_SUPPORT_ffff00009081a6b8
             ,uVar3);
LAB_ffff000090746be0:
  FUN_ffff00009077de54(s__CLKBUF_LK__Init_Done_ffff000090800cc0);
  return 0;
}


