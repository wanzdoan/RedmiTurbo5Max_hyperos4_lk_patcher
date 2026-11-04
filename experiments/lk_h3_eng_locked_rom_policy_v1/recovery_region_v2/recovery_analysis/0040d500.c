
/* android::base::LogMessage::LogMessage(char const*, unsigned int, android::base::LogSeverity, char
   const*, int) */

void __thiscall
android::base::LogMessage::LogMessage
          (LogMessage *this,char *param_1,undefined4 param_2,undefined4 param_4,long param_5,
          undefined4 param_6)

{
  undefined *puVar1;
  void *pvVar2;
  long lVar3;
  undefined8 uVar4;
  undefined *puVar5;
  long *plVar6;
  char *pcVar7;

  plVar6 = operator_new(0x128);
  puVar5 = PTR_vtable_00429b80;
  puVar1 = PTR_vtable_00429b80 + 0x40;
  lVar3 = *(long *)(PTR_VTT_00429b88 + 8);
  uVar4 = *(undefined8 *)(PTR_VTT_00429b88 + 0x10);
  plVar6[0xe] = (long)puVar1;
  *plVar6 = lVar3;
  *(undefined8 *)((long)plVar6 + *(long *)(lVar3 + -0x18)) = uVar4;
  pvVar2 = (void *)((long)plVar6 + *(long *)(*plVar6 + -0x18));
  std::ios_base::init(pvVar2);
  *(undefined8 *)((long)pvVar2 + 0x88) = 0;
  *(undefined4 *)((long)pvVar2 + 0x90) = 0xffffffff;
  *plVar6 = (long)(puVar5 + 0x18);
  plVar6[0xe] = (long)puVar1;
  std::streambuf::streambuf((streambuf *)(plVar6 + 1));
  puVar1 = PTR_vtable_00429b90 + 0x10;
  *(undefined4 *)(plVar6 + 0xd) = 0x10;
  plVar6[10] = 0;
  plVar6[9] = 0;
  plVar6[0xc] = 0;
  plVar6[0xb] = 0;
  plVar6[1] = (long)puVar1;
  pcVar7 = strrchr(param_1,0x2f);
  *(undefined4 *)(plVar6 + 0x22) = param_2;
  if (pcVar7 != (char *)0x0) {
    param_1 = pcVar7 + 1;
  }
  *(undefined4 *)((long)plVar6 + 0x114) = param_4;
  plVar6[0x23] = param_5;
  *(undefined4 *)(plVar6 + 0x24) = param_6;
  plVar6[0x21] = (long)param_1;
  *(long **)this = plVar6;
  return;
}
