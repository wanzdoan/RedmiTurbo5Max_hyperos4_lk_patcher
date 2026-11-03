
/* RfxMessage::obtainResponse(int, android::sp<RfxMessage> const&) */

void RfxMessage::obtainResponse(int param_1,sp *param_2)

{
  long lVar1;
  
  lVar1 = *(long *)param_2;
                    /* WARNING: Load size is inaccurate */
  obtainResponse(*(RfxMessage **)(lVar1 + 0x24),param_1,*(undefined4 *)(lVar1 + 0x2c),
                 *(undefined8 *)(lVar1 + 0x48),param_2);
  return;
}


