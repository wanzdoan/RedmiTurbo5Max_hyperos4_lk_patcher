// ffff000090783fd0  size=156  FUN_ffff000090783fd0

void FUN_ffff000090783fd0(void)

{
  int iVar1;
  undefined8 *puVar2;
  char *pcVar3;
  
  puVar2 = (undefined8 *)FUN_ffff00009071a348(s_pl_boottag_ffff0000907f8e0c,0);
  if ((puVar2 == (undefined8 *)0x0) || (iVar1 = FUN_ffff00009071a7f0(*puVar2), iVar1 == 0)) {
    puVar2 = (undefined8 *)FUN_ffff00009071a348(s_pl_drambuf_ffff0000907f377a,0);
    if ((puVar2 == (undefined8 *)0x0) || (iVar1 = FUN_ffff00009071a7f0(*puVar2), iVar1 == 0)) {
      return;
    }
    pcVar3 = s_pl_drambuf_ffff0000907f377a;
  }
  else {
    pcVar3 = s_pl_boottag_ffff0000907f8e0c;
  }
  FUN_ffff00009077e11c
            (s__s__d__free__s_failed_ffff0000907f5556,s_mrdump_free_reserved_ffff0000907e036d,0x3a,
             pcVar3);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_aee_mrd_ffff0000907ea042,0x3b,&DAT_ffff0000907d2506);
}


