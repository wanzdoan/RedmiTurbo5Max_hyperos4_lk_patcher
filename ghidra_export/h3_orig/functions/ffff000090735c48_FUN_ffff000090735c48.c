// ffff000090735c48  size=176  FUN_ffff000090735c48

void FUN_ffff000090735c48(void)

{
  long lVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  long lVar5;
  
  iVar2 = FUN_ffff0000907356e8();
  lVar3 = DAT_ffff0000908192c0;
  pcVar4 = DAT_ffff0000908192b8;
  lVar1 = DAT_ffff0000908192b0;
  if ((-1 < iVar2) &&
     (FUN_ffff00009077de54(s__PART_UTILS__dump_partition_tabl_ffff0000907f8146,DAT_ffff0000908192cc)
     , 0 < *(int *)(lVar1 + 0x50))) {
    lVar5 = 0;
    do {
      if ((*pcVar4 == '\0') && (pcVar4[1] == '\0')) {
        return;
      }
      FUN_ffff00009077de54
                (s__GPT___s___llu___llu___s_ffff0000907eac2f,lVar3,*(undefined8 *)(pcVar4 + 0x20),
                 *(undefined8 *)(pcVar4 + 0x28),lVar3 + 0x48);
      lVar5 = lVar5 + 1;
      lVar3 = lVar3 + 0x6d;
      pcVar4 = pcVar4 + 0x80;
    } while (lVar5 < *(int *)(lVar1 + 0x50));
  }
  return;
}


