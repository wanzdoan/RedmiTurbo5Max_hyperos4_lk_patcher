// ffff000090735ee0  size=176  FUN_ffff000090735ee0

void FUN_ffff000090735ee0(void)

{
  long lVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  long lVar5;
  
  iVar2 = FUN_ffff000090735980();
  lVar3 = DAT_ffff0000908192e0;
  pcVar4 = DAT_ffff0000908192d8;
  lVar1 = DAT_ffff0000908192d0;
  if ((-1 < iVar2) &&
     (FUN_ffff00009077e11c(s__PART_UTILS__dump_partition_tabl_ffff0000907f82e2,DAT_ffff0000908192ec)
     , 0 < *(int *)(lVar1 + 0x50))) {
    lVar5 = 0;
    do {
      if ((*pcVar4 == '\0') && (pcVar4[1] == '\0')) {
        return;
      }
      FUN_ffff00009077e11c
                (s__GPT___s___llu___llu___s_ffff0000907eadb2,lVar3,*(undefined8 *)(pcVar4 + 0x20),
                 *(undefined8 *)(pcVar4 + 0x28),lVar3 + 0x48);
      lVar5 = lVar5 + 1;
      lVar3 = lVar3 + 0x6d;
      pcVar4 = pcVar4 + 0x80;
    } while (lVar5 < *(int *)(lVar1 + 0x50));
  }
  return;
}


