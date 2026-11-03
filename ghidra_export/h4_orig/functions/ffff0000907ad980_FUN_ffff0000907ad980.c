// ffff0000907ad980  size=220  FUN_ffff0000907ad980

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907ad980(void)

{
  short sVar1;
  int iVar2;
  
  sVar1 = _DAT_ffff00009090c0e2;
  iVar2 = FUN_ffff00009077eae8(0xffff00009090c0d0,s_AND_ROMINFO_v_ffff0000907ea107,0xd);
  if (iVar2 != 0) {
    FUN_ffff00009077e11c
              (s___s__Rom_Info_Invalid_or_Not_Ini_ffff0000907f594d,s_SEC_ROMINFO_ffff0000907dbcaf);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d067a,0x57,&DAT_ffff0000907d2506);
  }
  if (sVar1 == 2) {
    FUN_ffff00009077e11c(s___s__Rom_Info_Ver____d_ffff0000907ea3d5,s_SEC_ROMINFO_ffff0000907dbcaf,2)
    ;
    DAT_ffff000090837eb4 = _DAT_ffff00009090c11c;
    DAT_ffff000090837eb8 = _DAT_ffff00009090c118;
    return 0;
  }
  FUN_ffff00009077e11c
            (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000907face6,s_SEC_ROMINFO_ffff0000907dbcaf,
             sVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907d067a,0x5b,&DAT_ffff0000907d2506);
}


